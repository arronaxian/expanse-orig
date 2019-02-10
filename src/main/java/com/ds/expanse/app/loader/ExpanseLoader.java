package com.ds.expanse.app.loader;

import com.ds.expanse.app.api.command.Command;
import com.ds.expanse.app.api.loader.Loader;
import com.ds.expanse.app.api.loader.model.*;
import com.ds.expanse.app.api.controller.model.*;
import com.ds.expanse.app.nlp.ExpanseNLP;
import com.ds.expanse.app.command.*;
import com.ds.expanse.app.repository.*;
import com.ds.expanse.app.api.loader.Mapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("ExpanseLoader")
public class ExpanseLoader implements Loader {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationTransitionRepository locationTransitionRepository;

    private final static Map<String, Item> itemsCacheById = new HashMap<>();
    private final static Map<String, Item> itemsCacheByName = new HashMap<>();
    private final static Map<String, ItemDO> itemsDOCacheById= new HashMap<>();
    private final static Map<String, NonPlayer> npcCacheById = new HashMap<>();

    private final static Map<String, LocationDO> locationDOCacheById = new HashMap<>();


    public static final ExpanseNLP nlp = new ExpanseNLP();

    public ExpanseLoader() {
    }

    @Override
    public Market getMarket() {
        return new Market(itemsCacheByName);
    }

    @Override
    public Map<String, NonPlayer> getNonPlayer() {
        return npcCacheById;
    }

    @Override
    public ExpanseNLP getNLP() {
        return nlp;
    }

    /**
     * Loads the game map, commands and non-player characters.
     * @throws IOException Is thrown if an error occurs.
     */
    @Override
    public void load() throws IOException {
        System.out.println("Expanse loading...");

        // DEBUG: clean up between runs to ensure things are working on initialization
        playerRepository.deleteAll();

        Map<String, Command> commandCache = new HashMap<>();
        loadItems();
        loadMap();
        loadMapIntoCache(commandCache);
        loadNPCIntoCache();

       // loadMapIntoCache(commandCache);

        System.out.println("Loading command interface...");
        commandCache.entrySet().forEach(es -> nlp.mapCommand(es.getKey(), es.getValue()));
        nlp.mapCommand("items", new ListItemsCommand("items"));
        nlp.mapCommand("look", new LookAtRoomCommand("look"));
        nlp.mapCommand("get", new TakeItemCommand("get"));
        nlp.mapCommand("drop", new DropItemCommand("drop"));
        nlp.mapCommand("buy", new BuyItemCommand("buy"));
        nlp.mapCommand("attack", new FightCommand("attack"));
        nlp.mapCommand("equip", new EquipItemCommand("equip"));
    }

    /**
     * Loads the default map from mapping file.
     * @throws IOException
     */
    private void loadMap() throws IOException {
        System.out.println("Persisting map...");

        // Remove all the existing locations.
        locationRepository.deleteAll();
        locationTransitionRepository.deleteAll();

        // locationId, list of location transition instances.
        Map<String, List<LocationTransitionDO>> locationTransitionCache = new HashMap<>();
        Map<String, LocationDO> locationCache = new HashMap<>();

        // Fetch map locations
        ObjectMapper mapper = new ObjectMapper();
        InputStream mapStream = ExpanseLoader.class.getResourceAsStream("/map.json");
        MapElementsDO locations = mapper.readerFor(MapElementsDO.class).readValue(mapStream);

        // Process each location data object
        locations.getLocations().forEach(ldo -> {
            // Set the location's items to persisted items.
            if ( ldo.hasItems() ) {
                List<ItemDO> persistedItemsList = ldo.getItems()
                        .stream()
                        .map(i -> itemsDOCacheById.get(i.getId()))
                        .collect(Collectors.toList());

                ldo.setItems(persistedItemsList);
            }

            // Keep a reference to each location transition so that the location instance can be persisted
            // generating the location id for database reference.
            if (ldo.hasLocationTransitions() ) {
                locationTransitionCache.put(ldo.getId(), ldo.getLocationTransitions());

                // This is done to facilitate inserting the location
                ldo.setLocationTransitions(new ArrayList<>());
            }

            // Persist the location and cache the save instance for lookup.
            final LocationDO persistedLocationDO = locationRepository.insert(ldo);

            locationCache.put(ldo.getId(), persistedLocationDO);
        });

        // Now process the location transitions.
        locationTransitionCache.entrySet().forEach(entry -> {
            String parentLocationId = entry.getKey();
            List<LocationTransitionDO> locationTransitions = entry.getValue();

            LocationDO fromLocationDO = locationCache.get(parentLocationId);
            locationTransitions.forEach(ltdo -> {
                LocationDO toLocationDO = locationCache.get(ltdo.getLocation().getId());
                ltdo.setLocation(toLocationDO);
                LocationTransitionDO persistedLocationTransitionDO = locationTransitionRepository.insert(ltdo);

                fromLocationDO.getLocationTransitions().add(persistedLocationTransitionDO);
            });

            locationRepository.save(fromLocationDO);
        });
    }

    /**
     * Loads the map locations into cache.
     * @param commandCache
     * @throws IOException
     */
    private void loadMapIntoCache(Map<String, Command> commandCache) throws IOException {
        System.out.println("Loading map...");
        Mapper mapper = new Mapper();

        // Fetch locations
        locationRepository.findAll().forEach(ldo -> {
            // Prepare cache.
            locationDOCacheById.put(ldo.getId(), ldo);

            Location location = mapper.toLocation(ldo);

            if ( location.getType() == Location.Type.wilderness ) {
                location.getLocationNonPlayers().addNonPlayer(getNonPlayer().get("1"));
            }

            location.getCommands().forEach(c -> commandCache.put(c.getValue().getTransition(), c.getKey()));

            if ( ldo.getItems() != null ) {
                ldo.getItems().forEach(i -> {
                    Item item = itemsCacheById.get(i.getId());
                    location.addItem(item);
                });
            }
        });
    }

    /**
     * Loads items into cache.
     * @throws IOException is thrown if an error occurs.
     */
    private void loadItems() throws IOException {
        System.out.println("Loading items into cache...");
        itemRepository.deleteAll();

        // Fetch items
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream itemsStream = ExpanseLoader.class.getResourceAsStream("/items.json");
        ItemsDO items = objectMapper.readerFor(ItemsDO.class).readValue(itemsStream);

        Mapper mapper = new Mapper();

        // Convert from DO into model.
        items.getItems().forEach(itemDO -> {
            ItemDO persistedItemDO = itemRepository.insert(itemDO);

            Item item = mapper.toItem(persistedItemDO);

            itemsCacheById.put(itemDO.getId(), item);
            itemsCacheByName.put(itemDO.getName(), item);

            itemsDOCacheById.put(itemDO.getId(), persistedItemDO);
        });
    }

    /**
     * Loads items into cache.
     * @throws IOException is thrown if an error occurs.
     */
    protected void loadNPCIntoCache() throws IOException {
        System.out.println("Loading non-players into cache...");

        // Fetch non-player characters
        ObjectMapper mapper = new ObjectMapper();
        InputStream itemsStream = ExpanseLoader.class.getResourceAsStream("/npc.json");
        NonPlayersDO npcs = mapper.readerFor(NonPlayersDO.class).readValue(itemsStream);

        // Convert from DO into model.
        npcs.getNonPlayers().forEach(npcDO -> {
            NonPlayer npc = new NonPlayer(npcDO.getName());
            npc.setId(npcDO.getId());
            npc.setName(npcDO.getName());
            npc.setDescription(npcDO.getDescription());
            npc.setType(Player.Type.valueOf(npcDO.getType()));
            npc.setHealth(npcDO.getHealth());
            npc.setCoins(0);

            npc.addItem(itemsCacheByName.get(npcDO.getDrop()));

            npcCacheById.put(npc.getId(), npc);
        });
    }
}
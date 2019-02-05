package com.ds.expanse.app.api.loader;

import com.ds.expanse.app.api.controller.model.*;
import com.ds.expanse.app.api.loader.model.*;
import com.ds.expanse.app.command.TransitionCommand;

import java.util.*;
import java.util.stream.Collectors;

public class Mapper {
    protected Map<String, Player> playerCache = Collections.synchronizedMap(new HashMap<>());
    protected Map<String, Item> itemCache = Collections.synchronizedMap(new HashMap<>());
    protected Map<String, Location> locationCache = Collections.synchronizedMap(new HashMap<>());
    protected Map<String, LocationTransition> locationTransitionCache = Collections.synchronizedMap(new HashMap<>());

    public Map<String, Player> getPlayerCache() {
        return playerCache;
    }

    public Map<String, Item> getItemCache() {
        return itemCache;
    }

    public Map<String, Location> getLocationCache() {
        return locationCache;
    }

    public Map<String, LocationTransition> getLocationTransitionCache() {
        return locationTransitionCache;
    }

    public void clear() {
        getPlayerCache().clear();
        getItemCache().clear();
        getLocationCache().clear();
        getLocationTransitionCache().clear();
    }

    public ItemDO toItemDO(Item item) {
        final ItemDO itemDO;
        if ( item == null ) {
            itemDO = null;
        } else {
            itemDO = new ItemDO();

            itemDO.setId(item.getId());
            itemDO.setName(item.getName());
            itemDO.setDescription(item.getDescription());
            itemDO.setCost(item.getCost());
            itemDO.setCount(item.getCount());
            itemDO.setType(item.getType().name());
            itemDO.setCriticalHitChance(item.getCriticalHitChance());
            itemDO.setCriticalHitDamage(item.getHitDamage());
            itemDO.setHitDamage(item.getHitDamage());
        }

        return itemDO;
    };

    public Item toItem(ItemDO itemDO) {
        final Item item;
        if ( itemDO == null ) {
            item = null;
        } else {
            item = itemCache.getOrDefault(itemDO.getId(), new Item());

            item.setId(itemDO.getId());
            item.setName(itemDO.getName());
            item.setDescription(itemDO.getDescription());
            item.setCost(itemDO.getCost());
            item.setCount(itemDO.getCount());
            item.setType(Item.Type.valueOf(itemDO.getType()));
            item.setCriticalHitChance(itemDO.getCriticalHitChance());
            item.setCriticalHitDamage(itemDO.getHitDamage());
            item.setHitDamage(itemDO.getHitDamage());
        }

        return item;
    };

    public Location toLocation(LocationDO locationDO) {
        return toLocation(locationDO, new HashMap<>());
    }

    public Location toLocation(LocationDO locationDO, Map<LocationDO, Location> visitedLocations) {
        final Location location;
        if ( locationDO == null ) {
            location = null;
        } else {
            if ( visitedLocations.containsKey(locationDO) ) {
                location = visitedLocations.get(locationDO);
            } else {
                location = new Location();

                location.setId(locationDO.getId());
                location.setName(locationDO.getName());
                location.setDescription(locationDO.getDescription());
                location.setMap(locationDO.getMapx(), locationDO.getMapy());
                location.setType(Location.Type.valueOf(locationDO.getType()));

                visitedLocations.put(locationDO, location);

                locationDO.getItems().forEach(itemDO -> {
                    location.addItem(toItem(itemDO));
                });

                locationDO.getLocationTransitions().forEach(locationTransitionDO -> {
                    final TransitionCommand moveCommand = new TransitionCommand(locationTransitionDO.getTransition());

                    final LocationTransition locationTransition = new LocationTransition();
                    locationTransition.setId(locationTransitionDO.getId());

                    final Location cachedLocation;
                    if ( visitedLocations.containsKey(locationTransitionDO.getLocation() )) {
                        cachedLocation = visitedLocations.get(locationTransitionDO.getLocation());
                    } else {
                        cachedLocation = toLocation(locationTransitionDO.getLocation(), visitedLocations);
                        visitedLocations.put(locationDO, cachedLocation);
                    }
                    locationTransition.setLocation(cachedLocation);

                    location.addTransition(moveCommand, toLocationTransition(locationTransitionDO, visitedLocations));
                });
            }
        }

        return location;
    };

    public LocationTransition toLocationTransition(LocationTransitionDO locationTransitionDO) {
        return toLocationTransition(locationTransitionDO, new HashMap<>());
    }

    protected LocationTransition toLocationTransition (LocationTransitionDO locationTransitionDO, Map<LocationDO, Location> visistedLocations) {
        final LocationTransition locationTransition;
        if ( locationTransitionDO == null ) {
            locationTransition = null;
        } else {
            locationTransition = new LocationTransition();
            locationTransition.setId(locationTransitionDO.getId());
            locationTransition.setDescription(locationTransitionDO.getDescription());
            locationTransition.setTransition(locationTransitionDO.getTransition());

            final Location location;
            if ( visistedLocations.containsKey(locationTransitionDO.getLocation()) ) {
                location = visistedLocations.get(locationTransitionDO.getLocation());
            } else {
                location = toLocation(locationTransitionDO.getLocation());
                visistedLocations.put(locationTransitionDO.getLocation(), location);
            }
            locationTransition.setLocation(location);
        }

        return locationTransition;
    };

    public LocationTransition toLocationTransition (LocationTransitionDO locationTransitionDO, HashMap<LocationDO, Location> visistedLocations) {
        final LocationTransition locationTransition;
        if ( locationTransitionDO == null ) {
            locationTransition = null;
        } else {

            locationTransition = new LocationTransition();
            locationTransition.setId(locationTransitionDO.getId());
            locationTransition.setDescription(locationTransitionDO.getDescription());
            locationTransition.setTransition(locationTransitionDO.getTransition());

            final Location location = visistedLocations.containsKey(locationTransitionDO.getLocation()) ?
                visistedLocations.get(locationTransitionDO.getLocation()) :
                toLocation(locationTransitionDO.getLocation());

            locationTransition.setLocation(location);
        }

        return locationTransition;
    };

    public LocationTransitionDO toLocationTransitionDO(LocationTransition locationTransition) {
        return toLocationTransitionDO(locationTransition, new HashMap<>());
    };

    protected LocationTransitionDO toLocationTransitionDO(LocationTransition locationTransition, Map<Location, LocationDO> visistedLocations) {
        final LocationTransitionDO locationTransitionDO;
        if ( locationTransition == null ) {
            locationTransitionDO = null;
        } else {
            locationTransitionDO = new LocationTransitionDO();
            locationTransitionDO.setId(locationTransition.getId());
            locationTransitionDO.setDescription(locationTransition.getDescription());
            locationTransitionDO.setTransition(locationTransition.getTransition());

            final LocationDO locationDO;
            if ( visistedLocations.containsKey(locationTransition.getLocation()) ) {
                locationDO = visistedLocations.get(locationTransition.getLocation());
            } else {
                locationDO = toLocationDO(locationTransition.getLocation(), visistedLocations);
                visistedLocations.put(locationTransition.getLocation(), locationDO);
            }
            locationTransitionDO.setLocation(locationDO);
        }

        return locationTransitionDO;
    };


    public LocationDO toLocationDO(Location location) {
        return toLocationDO(location, new HashMap<>());
    }

    public LocationDO toLocationDO(Location location, Map<Location, LocationDO> visistedLocations) {
        final LocationDO locationDO;
        if ( location == null ) {
            locationDO = null;
        } else {
            locationDO = new LocationDO();
            locationDO.setId(location.getId());
            locationDO.setName(location.getName());
            locationDO.setDescription(location.getDescription());
            locationDO.setMapx(location.getMapx());
            locationDO.setMapy(location.getMapy());
            locationDO.setType(location.getType().name());

            location.getItems().forEach(item -> {
                locationDO.getItems().add(toItemDO(item));
            });

            visistedLocations.put(location, locationDO);

            location.getTransitions().forEach(transition -> {
                locationDO.getLocationTransitions().add(toLocationTransitionDO(transition, visistedLocations));
            });
        }

        return locationDO;
    };

    public PlayerDO toPlayerDO(Player player) {
        final PlayerDO playerDO;
        if ( player == null ) {
            playerDO = null;
        } else {
            playerDO = new PlayerDO();
            playerDO.setId(player.getId());
            playerDO.setName(player.getName());
            playerDO.setCoins(player.getCoins());
            playerDO.setHealth(player.getHealth());

            // process the items
            player.getItemList().forEach(i -> {
                playerDO.getItems().add(toItemDO(i));
            });

            // process visited locations
            List<LocationDO> locations = player.getVisitedLocations().values()
                    .stream()
                    .map(vl -> toLocationDO(vl))
                    .collect(Collectors.toList());
            playerDO.setVisitedLocations(locations);

            // process current location
            LocationDO currentLocationDO = playerDO.getVisitedLocations().stream()
                    .filter(l -> l.getId().equals(player.getCurrentLocation().getId()))
                    .findFirst().get();

            playerDO.setCurrentLocation(currentLocationDO);

        }

        return playerDO;
    };

    public Player toPlayer(PlayerDO playerDO) {
        final Player player;
        if ( playerDO == null ) {
            player = null;
        } else {
            player = playerCache.getOrDefault(playerDO.getId(), new Player(playerDO.getName()));

            player.setId(playerDO.getId());
            player.setCoins(playerDO.getCoins());
            player.setHealth(playerDO.getHealth());

            playerDO.getItems().forEach(i -> {
                player.addItem(toItem(i));
            });

            List<Location> locations = playerDO.getVisitedLocations()
                    .stream()
                    .map(vl -> toLocation(vl))
                    .collect(Collectors.toList());
            locations.forEach(l -> player.getVisitedLocations().put(l.getId(), l));

            Location location = toLocation(playerDO.getCurrentLocation());
            if ( location != null ) {
                player.setCurrentLocation(location);
            }
        }

        return player;
    };
}

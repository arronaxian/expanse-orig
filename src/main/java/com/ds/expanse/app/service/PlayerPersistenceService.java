package com.ds.expanse.app.service;

import com.ds.expanse.app.api.controller.model.Location;
import com.ds.expanse.app.api.loader.model.LocationDO;
import com.ds.expanse.app.api.loader.model.PlayerAlteredLocationDO;
import com.ds.expanse.app.api.loader.model.PlayerVisitedLocationDO;
import com.ds.expanse.app.api.service.PlayerService;
import com.ds.expanse.app.api.loader.model.PlayerDO;
import com.ds.expanse.app.api.controller.model.Player;
import com.ds.expanse.app.repository.LocationRepository;
import com.ds.expanse.app.repository.PlayerAlteredLocationRepository;
import com.ds.expanse.app.repository.PlayerRepository;
import com.ds.expanse.app.repository.PlayerVisitedLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.logging.Logger;

import static com.ds.expanse.app.api.loader.Mapper.mapper;

@Service("PlayerPersistenceService")
public class PlayerPersistenceService implements PlayerService {
    private final static Logger LOG = Logger.getLogger(PlayerPersistenceService.class.getName());

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PlayerAlteredLocationRepository playerAlteredLocationRepository;

    @Autowired
    private PlayerVisitedLocationRepository playerVisitedLocationRepository;

    @Override
    public Player createTemporaryPlayer() {
        // Build the temporary player base information
        PlayerDO playerDO = new PlayerDO();
        playerDO.setName(RandomNameGenerator.build());
        playerDO.setEmailAddress("temporary");
        playerDO.setUid(UUID.randomUUID().toString());
        playerDO.setHealth(100);
        playerDO.setCoins(100);

        // Build the player location and store off the location location.
        LocationDO firstLocation = locationRepository.findById("1").get();
        playerDO.setCurrentLocation(firstLocation);

        playerDO = save(playerDO, false);

        LOG.info("Creating new user " + playerDO.getName());

        // Map and return
        return mapper.toPlayer(playerDO);
    }

    @Override
    public Player save(Player player) {
        return mapper.toPlayer(save(mapper.toPlayerDO(player), player.getCurrentLocation().isAltered()));
    }

    @Override
    public Player findPlayerByName(String name) {
        PlayerDO playerDO = playerRepository.findByName(name);

        if ( playerDO != null ) {
            Player player = mapper.toPlayer(playerDO);

            // TODO: clean this up - should take PlayerDO as well
            Location visistedLocation = findVisitedPlayerLocation(player, playerDO.getCurrentLocation().getId());
            player.setCurrentLocation(visistedLocation);

            return player;
        } else {
            return null;
        }
    }

    @Override
    public Location findAlteredPlayerLocation(Player player, Location toLocation) {
        Location location = findAlteredPlayerLocation(player, toLocation.getId());
        return location == null ? toLocation : location;
    }

    @Override
    public Location findVisitedPlayerLocation(Player player, String locationId) {
        // Look up the location location to get the original map location reference.
        PlayerVisitedLocationDO visitedLocation = playerVisitedLocationRepository.findByPlayerIdAndLocationId(player.getId(), locationId);

        if ( visitedLocation != null ) {
            // Then see if the location location references an altered location.
            Location alteredLocation = findAlteredPlayerLocation(player, visitedLocation.getLocation().getId());
            if (alteredLocation == null) {
                return mapper.toLocation(visitedLocation.getLocation());
            } else {
                LOG.info("Found altered location " + alteredLocation);
                return alteredLocation;
            }
        } else {
            throw new IllegalArgumentException("Location does not exist.");
        }
    }

    protected Location findAlteredPlayerLocation(Player player, String toLocationId) {


        PlayerAlteredLocationDO playerLocation = playerAlteredLocationRepository.findByPlayerIdAndLocationId(player.getId(), toLocationId);
        final Location location;
        if ( playerLocation == null ) {
            return null;
        } else {
            location = mapper.toLocation(playerLocation);
            location.setId(playerLocation.getLocation().getId());
        }

        return location;
    }

    /**
     * Saves the player location location based on the current location.
     * @param player
     */
    protected void savePlayerVisitedLocation(PlayerDO player) {
        PlayerVisitedLocationDO visitedLocationDO = playerVisitedLocationRepository
                .findByPlayerIdAndLocationId(player.getId(), player.getCurrentLocation().getId());
        if ( visitedLocationDO == null ) {
            visitedLocationDO = new PlayerVisitedLocationDO();
            visitedLocationDO.setPlayer(player);
            visitedLocationDO.setLocation(player.getCurrentLocation());

            playerVisitedLocationRepository.insert(visitedLocationDO);
        }
    }

    /**
     * Saves the player.
     * @param playerDO Saves the player.
     * @return
     */
    protected PlayerDO save(PlayerDO playerDO, boolean isCurrentLocationAltered) {
        if ( playerDO.isNew() ) {
            PlayerDO exists = playerRepository.findByName(playerDO.getName());
            if ( exists != null ) {
                throw new IllegalArgumentException("Player name is already taken.");
            }

            playerDO = playerRepository.insert(playerDO);
        } else {
            playerDO = playerRepository.save(playerDO);
        }

        // Save the player current location if altered.
        if ( isCurrentLocationAltered ) {
            saveAlteredPlayerLocation(playerDO);
        }

        // Save the player's location locations.
        savePlayerVisitedLocation(playerDO);

        return playerDO;
    }

    /**
     * Saves the player's location.
     * @param player The player instance with current location.
     */
    protected void saveAlteredPlayerLocation(PlayerDO player) {
        PlayerAlteredLocationDO savedPlayerLocation = playerAlteredLocationRepository
                .findByPlayerIdAndLocationId(player.getId(), player.getCurrentLocation().getId());
        PlayerAlteredLocationDO playerLocation = createAlteredPlayerLocation(player);

        String playerLocationId = savedPlayerLocation == null ? null : savedPlayerLocation.getId();
        playerLocation.setId(playerLocationId);

        if ( playerLocation.getId() == null ) {
            playerAlteredLocationRepository.insert(playerLocation);
        } else {
            playerAlteredLocationRepository.save(playerLocation);
        }
    }

    private PlayerAlteredLocationDO createAlteredPlayerLocation(PlayerDO playerDO) {
        PlayerAlteredLocationDO playerLocationDO = new PlayerAlteredLocationDO();

        playerLocationDO.setPlayer(playerDO);
        playerLocationDO.setLocation(playerDO.getCurrentLocation());

        LocationDO location = playerDO.getCurrentLocation();
        playerLocationDO.setId(location.getId());
        playerLocationDO.setName(location.getName());
        playerLocationDO.setDescription(location.getDescription());
        playerLocationDO.setMapx(location.getMapx());
        playerLocationDO.setMapy(location.getMapy());
        playerLocationDO.setItems(location.getItems());
        playerLocationDO.setLocationTransitions(location.getLocationTransitions());
        playerLocationDO.setType(location.getType());

        return playerLocationDO;
    }
}

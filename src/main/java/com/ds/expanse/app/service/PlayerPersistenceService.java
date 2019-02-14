package com.ds.expanse.app.service;

import com.ds.expanse.app.api.controller.model.Location;
import com.ds.expanse.app.api.loader.Mapper;
import com.ds.expanse.app.api.loader.model.LocationDO;
import com.ds.expanse.app.api.loader.model.PlayerLocationDO;
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

import static com.ds.expanse.app.api.loader.Mapper.mapper;

@Service("PlayerPersistenceService")
public class PlayerPersistenceService implements PlayerService {
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

        // Build the player location and store off the visited location.
        LocationDO firstLocation = locationRepository.findById("1").get();
        playerDO.setCurrentLocation(firstLocation);

        playerDO = save(playerDO);

        // Map and return
        return mapper.toPlayer(playerDO);
    }

    @Override
    public Player save(Player player) {
        PlayerDO playerDO = mapper.toPlayerDO(player);

        Player savedPlayer = mapper.toPlayer(save(playerDO));

        // Save the player current location if altered.
        if ( player.getCurrentLocation().isAltered() ) {
            saveAlteredPlayerLocation(playerDO);
        }

        // Save the player's visited locations.
        savePlayerVisitedLocation(playerDO);

        return savedPlayer;
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
        // Look up the visited room
        PlayerVisitedLocationDO visitedLocation = playerVisitedLocationRepository.findByPlayerIdAndLocationId(player.getId(), locationId);

        // Then see if the visited location was modified, if so use it.  Otherwise use the visited location.
        Location location = findAlteredPlayerLocation(player, visitedLocation.getId());
        if ( location == null ) {
            return mapper.toLocation(visitedLocation.getLocation());
        } else {
            return location;
        }
    }

    protected Location findAlteredPlayerLocation(Player player, String toLocationId) {
        PlayerLocationDO playerLocation = playerAlteredLocationRepository.findByPlayerIdAndLocationId(player.getId(), toLocationId);
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
     * Saves the player.
     * @param playerDO Saves the player.
     * @return
     */
    protected PlayerDO save(PlayerDO playerDO) {
        if ( playerDO.isNew() ) {
            PlayerDO exists = playerRepository.findByName(playerDO.getName());
            if ( exists != null ) {
                throw new IllegalArgumentException("Player name is already taken.");
            }

            playerDO = playerRepository.insert(playerDO);
        } else {
            playerDO = playerRepository.save(playerDO);
        }

        return playerDO;
    }

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
     * Saves the player's location.
     * @param player The player instance with current location.
     */
    private void saveAlteredPlayerLocation(PlayerDO player) {
        PlayerLocationDO savePlayerLocation = playerAlteredLocationRepository.findByPlayerIdAndLocationId(player.getId(), player.getCurrentLocation().getId());
        PlayerLocationDO playerLocation = createAlteredPlayerLocation(player);

        String playerLocationId = savePlayerLocation == null ? null : savePlayerLocation.getId();
        playerLocation.setId(playerLocationId);

        if ( playerLocation.getId() == null ) {
            playerAlteredLocationRepository.insert(playerLocation);
        } else {
            playerAlteredLocationRepository.save(playerLocation);
        }
    }

    private PlayerLocationDO createAlteredPlayerLocation(PlayerDO playerDO) {
        PlayerLocationDO playerLocationDO = new PlayerLocationDO();

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

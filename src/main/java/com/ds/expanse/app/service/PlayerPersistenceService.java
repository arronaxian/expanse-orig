package com.ds.expanse.app.service;

import com.ds.expanse.app.api.loader.Mapper;
import com.ds.expanse.app.api.loader.model.LocationDO;
import com.ds.expanse.app.api.loader.model.PlayerLocationDO;
import com.ds.expanse.app.api.service.PlayerService;
import com.ds.expanse.app.api.loader.model.PlayerDO;
import com.ds.expanse.app.api.controller.model.Player;
import com.ds.expanse.app.repository.LocationRepository;
import com.ds.expanse.app.repository.PlayerLocationRepository;
import com.ds.expanse.app.repository.PlayerRepository;
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
    private PlayerLocationRepository playerLocationRepository;

    @Override
    public Player createTemporaryPlayer() {
        // Build the temporary player base information
        PlayerDO playerDO = new PlayerDO();
        playerDO.setName(RandomNameGenerator.build());
        playerDO.setEmailAddress("temporary");
        playerDO.setUid(UUID.randomUUID().toString());
        playerDO.setHealth(100);
        playerDO.setCoins(100);
        playerDO = playerRepository.insert(playerDO);

        // Build the player location and store off the visited location.
        LocationDO firstLocation = locationRepository.findById("1").get();
        // Set the current and visited location
        playerDO.setCurrentLocation(firstLocation);

        playerDO = save(playerDO);

        // Map and return
        return mapper.toPlayer(playerDO);
    }

    @Override
    public Player save(Player player) {
        PlayerDO playerDO = mapper.toPlayerDO(player);

        Player savedPlayer = mapper.toPlayer(save(playerDO));

        if ( player.getCurrentLocation().isAltered() ) {
            savePlayerCurrentLocation(playerDO);
        }

        return savedPlayer;
    }

    /**
     * Saves the player.
     * @param playerDO Saves the player.
     * @return
     */
    private PlayerDO save(PlayerDO playerDO) {
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

    /**
     * Saves the player's location.
     * @param player The player instance with current location.
     */
    private void savePlayerCurrentLocation(PlayerDO player) {
        PlayerLocationDO savePlayerLocation = playerLocationRepository.findByPlayerIdAndLocationId(player.getId(), player.getCurrentLocation().getId());
        PlayerLocationDO playerLocation = createPlayerLocationDO(player);
        playerLocation.setId(savePlayerLocation.getId());

        if ( playerLocation.getId() == null ) {
            playerLocationRepository.insert(playerLocation);
        } else {
            playerLocationRepository.save(playerLocation);
        }
    }

    private PlayerLocationDO createPlayerLocationDO(PlayerDO playerDO) {
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

        return playerLocationDO;
    }



    @Override
    public Player findByName(String name) {
        Mapper mapper = new Mapper();
        PlayerDO playerDO = playerRepository.findByName(name);
        return playerDO == null ? null : mapper.toPlayer(playerDO);
    }
}

package com.ds.expanse.app.service;

import com.ds.expanse.app.api.loader.Mapper;
import com.ds.expanse.app.api.loader.model.LocationDO;
import com.ds.expanse.app.api.loader.model.PlayerMapDO;
import com.ds.expanse.app.api.service.PlayerService;
import com.ds.expanse.app.api.loader.model.PlayerDO;
import com.ds.expanse.app.api.controller.model.Player;
import com.ds.expanse.app.repository.LocationRepository;
import com.ds.expanse.app.repository.PlayerMapRepository;
import com.ds.expanse.app.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("PlayerPersistenceService")
public class PlayerPersistenceService implements PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PlayerMapRepository playerMapRepository;

    @Override
    public Player createTemporaryPlayer() {
        Mapper mapper = new Mapper();

        // Build the temporary player base information
        PlayerDO playerDO = new PlayerDO();
        playerDO.setName(RandomNameGenerator.build());
        playerDO.setEmailAddress("temporary");
        playerDO.setUid(UUID.randomUUID().toString());
        playerDO.setHealth(100);
        playerDO.setCoins(100);
        playerDO = playerRepository.insert(playerDO);

        // Build the player location and store off the visited location.
        PlayerMapDO mapDO = new PlayerMapDO();
        LocationDO firstLocation = locationRepository.findById("1").get();
        mapDO.setPlayer(playerDO);
        mapDO.setVisitedLocation(firstLocation);
        mapDO = playerMapRepository.insert(mapDO);

        // Set the current and visited location
        playerDO.setCurrentLocation(firstLocation);
        playerDO.getVisitedLocations().add(mapDO);
        playerDO = playerRepository.save(playerDO);

        // Map and return
        return mapper.toPlayer(playerDO);
    }

    @Override
    public Player save(Player player) {
        Mapper mapper = new Mapper();

        PlayerDO playerDO = mapper.toPlayerDO(player);
        if ( player.isNew() ) {
            PlayerDO exists = playerRepository.findByName(playerDO.getName());
            if ( exists != null ) {
                throw new IllegalArgumentException("Player name is already taken.");
            }

            playerDO = playerRepository.insert(playerDO);
        } else {
            List<PlayerMapDO> playerMapDOs = playerDO.getVisitedLocations().stream()
                    .map(pm -> {
                        if ( pm.getId() == null ) {
                            return playerMapRepository.insert(pm);
                        } else {
                            return playerMapRepository.save(pm);
                        }
                    }).collect(Collectors.toList());
            playerDO.setVisitedLocations(playerMapDOs);



            playerDO = playerRepository.save(playerDO);
        }

        return mapper.toPlayer(playerDO);
    }

    @Override
    public Player findByName(String name) {
        Mapper mapper = new Mapper();
        PlayerDO playerDO = playerRepository.findByName(name);
        return playerDO == null ? null : mapper.toPlayer(playerDO);
    }
}

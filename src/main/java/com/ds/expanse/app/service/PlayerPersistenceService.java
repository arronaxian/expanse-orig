package com.ds.expanse.app.service;

import com.ds.expanse.app.api.loader.Mapper;
import com.ds.expanse.app.api.loader.model.LocationDO;
import com.ds.expanse.app.api.service.PlayerService;
import com.ds.expanse.app.api.loader.model.PlayerDO;
import com.ds.expanse.app.api.controller.model.Player;
import com.ds.expanse.app.repository.LocationRepository;
import com.ds.expanse.app.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import static com.ds.expanse.app.api.loader.Mapper.mapper;

@Service("PlayerPersistenceService")
public class PlayerPersistenceService implements PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private LocationRepository locationRepository;

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
        return mapper.toPlayer(save(mapper.toPlayerDO(player)));
    }

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


    @Override
    public Player findByName(String name) {
        Mapper mapper = new Mapper();
        PlayerDO playerDO = playerRepository.findByName(name);
        return playerDO == null ? null : mapper.toPlayer(playerDO);
    }
}

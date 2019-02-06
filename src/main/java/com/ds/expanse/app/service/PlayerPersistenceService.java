package com.ds.expanse.app.service;

import com.ds.expanse.app.api.controller.model.Location;
import com.ds.expanse.app.api.loader.Mapper;
import com.ds.expanse.app.api.loader.model.LocationDO;
import com.ds.expanse.app.api.loader.model.PlayerMapDO;
import com.ds.expanse.app.api.service.PlayerService;
import com.ds.expanse.app.api.loader.model.PlayerDO;
import com.ds.expanse.app.api.controller.model.Player;
import com.ds.expanse.app.loader.ExpanseLoader;
import com.ds.expanse.app.repository.LocationRepository;
import com.ds.expanse.app.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("PlayerPersistenceService")
public class PlayerPersistenceService implements PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public Player createTemporaryPlayer() {
        Mapper mapper = new Mapper();

        PlayerDO playerDO = new PlayerDO();
        playerDO.setName(RandomNameGenerator.build());
        playerDO.setEmailAddress("temporary");
        playerDO.setUid(UUID.randomUUID().toString());
        playerDO.setHealth(100);
        playerDO.setCoins(100);

        LocationDO firstLocation = locationRepository.findById("1").get();

        PlayerMapDO mapDO = new PlayerMapDO();
        mapDO.setPlayer(playerDO);
        mapDO.setLocation(firstLocation);
        mapDO.setVisistedLocations(firstLocation);

        playerDO.setCurrentLocation(firstLocation);
        playerDO.getVisitedLocations().add(mapDO);

        playerDO = playerRepository.save(playerDO);

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

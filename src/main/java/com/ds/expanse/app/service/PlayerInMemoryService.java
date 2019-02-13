package com.ds.expanse.app.service;

import com.ds.expanse.app.api.controller.model.Location;
import com.ds.expanse.app.api.service.PlayerService;
import com.ds.expanse.app.api.controller.model.Player;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service("PlayerInMemoryService")
public class PlayerInMemoryService implements PlayerService {
    private static final Map<String, Player> PLAYER_CACHE = Collections.synchronizedMap( new HashMap<>() );

    @Override
    public Player createTemporaryPlayer() {
        return null;
    }

    public Player save(Player player) {
        PLAYER_CACHE.put(player.getName(), player);

        return player;
    }

    public Player findByName(String name) {
        return PLAYER_CACHE.get(name);
    }

    @Override
    public Location findPlayerLocation(Player player, Location toLocation) {
        return player.getCurrentLocation();
    }
}

package com.ds.expanse.app.api.service;

import com.ds.expanse.app.api.controller.model.Player;

public interface PlayerService {
    Player createTemporaryPlayer();
    Player save(Player player);
    Player findByName(String name);
}

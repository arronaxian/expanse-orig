package com.ds.expanse.app.controller;

import com.ds.expanse.app.api.controller.model.Item;
import com.ds.expanse.app.api.controller.model.Player;
import com.ds.expanse.app.api.service.PlayerService;
import com.ds.expanse.app.loader.ExpanseLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(value = "/player", produces = "application/hal+json")
public class PlayerController {
    @Autowired
    @Qualifier("PlayerPersistenceService")
    PlayerService playerService;

    @Autowired
    ExpanseLoader loader;

    @PutMapping(path="/login")
    public ResponseEntity login(@RequestParam(value="username", defaultValue="") String userName,
                                @RequestParam(value="password") String password) {
        return ResponseEntity.ok().build();
    }

    @GetMapping(path="/readyplayerone")
    public ResponseEntity<Player> readyPlayerOne(@RequestHeader(value="X-Expanse-User") String user) {
        Player player = playerService.findPlayerByName(user);
        if ( player == null ) {
            player = playerService.createTemporaryPlayer();
        }
        return ResponseEntity.ok(player);
    }

    @GetMapping("/inventory/current")
    public ResponseEntity<Collection<Item>> inventory(@RequestHeader(value="X-Expanse-User") String user) {
        Player player = playerService.findPlayerByName(user);
        if ( player != null ) {
            return ResponseEntity.ok(player.getItemList());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

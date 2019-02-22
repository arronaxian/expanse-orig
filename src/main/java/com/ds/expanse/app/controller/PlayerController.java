package com.ds.expanse.app.controller;

import com.ds.expanse.app.api.controller.model.Player;
import com.ds.expanse.app.api.service.PlayerService;
import com.ds.expanse.app.controller.resourcesupport.InventoryBodyResourceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Player REST controller providing player login, temporary play and general information.
 */
@RestController
@RequestMapping(value = "/player", produces = "application/hal+json")
public class PlayerController {
    @Autowired
    @Qualifier("PlayerPersistenceService")
    PlayerService playerService;

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

    /**
     * Gets the current inventory for the user.
     * @param user The user's inventory.
     * @return A list of inventory items.
     */
    @GetMapping("/inventory/current")
    public ResponseEntity<InventoryBodyResourceSupport> inventory(@RequestHeader(value="X-Expanse-User") String user) {
        Player player = playerService.findPlayerByName(user);
        InventoryBodyResourceSupport resourceSupport = new InventoryBodyResourceSupport(player.getItemList());
        if ( player != null ) {
            return ResponseEntity.ok(resourceSupport);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}

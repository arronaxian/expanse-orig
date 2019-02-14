package com.ds.expanse.app.controller;

import com.ds.expanse.app.api.controller.model.Location;
import com.ds.expanse.app.api.loader.Loader;
import com.ds.expanse.app.api.controller.model.Item;
import com.ds.expanse.app.api.controller.model.Player;
import com.ds.expanse.app.api.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(value = "/inventory", produces = "application/hal+json")
public class InventoryController {
    @Autowired
    @Qualifier("PlayerPersistenceService")
    PlayerService playerService;

    @Autowired
    Loader loader;

    @GetMapping("map/current")
    public ResponseEntity<InventoryBodyResourceSupport> mapCurrent(@RequestHeader(value="X-Expanse-User") String user) {
        Player player = playerService.findPlayerByName(user);
        if ( player != null ) {
            String description = player.getCurrentLocation().getItems().stream()
                    .map(items -> items.getDescription())
                    .collect(Collectors.joining(" ,"));

            InventoryBodyResourceSupport inventoryBody = new InventoryBodyResourceSupport(description);

            return ResponseEntity.ok(inventoryBody);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("player/current")
    public ResponseEntity<Collection<Item>> playerCurrent(@RequestHeader(value="X-Expanse-User") String user) {
        Player player = playerService.findPlayerByName(user);
        if ( player != null ) {
            return ResponseEntity.ok(player.getItemList());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/market")
    public ResponseEntity<Collection<Item>> market(@RequestHeader(value="X-Expanse-User") String user) {
        Player player = playerService.findPlayerByName(user);
        if ( player != null ) {
            return ResponseEntity.ok(loader.getMarket().getItems(player));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

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

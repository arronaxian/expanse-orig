package com.ds.expanse.app.controller;

import com.ds.expanse.app.command.CommandResult;
import com.ds.expanse.app.api.controller.model.Location;
import com.ds.expanse.app.api.controller.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;
import com.ds.expanse.app.api.service.PlayerService;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * MapElements controller provides access to the map resources.
 */
@RestController
@RequestMapping(value = "/map", produces = "application/hal+json")
public class MapController {
    @Autowired
    @Qualifier("PlayerPersistenceService")
    PlayerService playerService;

    /**
     * The current map location.
     *
     * @return A description of the mapCurrent map location.
     */
    @GetMapping("/current")
    public ResponseEntity<MapBodyResourceSupport> current(@RequestHeader(value="X-Expanse-User") String user) {
        Player player = playerService.findPlayerByName(user);
        if ( player != null ) {
            final Location location = player.getCurrentLocation();
            MapBodyResourceSupport mapBody = new MapBodyResourceSupport(location.getDescription());

            // Add links for transition details if there are any.  Evil trick to have no transitions out.
            if ( !player.getCurrentLocation().getTransitions().isEmpty() ) {
                mapBody.add(linkTo(methodOn(MapController.class).transitions(user)).withRel(CommandResult.Type.transition.toString()));
            }

            // Add links for location items details.
            if ( !player.getCurrentLocation().getItems().isEmpty() ) {
                mapBody.add(linkTo(methodOn(MapController.class).items(user)).withRel(CommandResult.Type.locationitem.toString()));
            }

            // Add link for location location details
            mapBody.add(linkTo(methodOn(MapController.class).location(user, location.getId())).withRel(CommandResult.Type.location.toString()));

            return ResponseEntity.ok(mapBody);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * The map's current location transition details.
     *
     * @return Details as text.
     */
    @GetMapping("/location/transitions/current")
    public ResponseEntity<MapBodyResourceSupport> transitions(@RequestHeader(value="X-Expanse-User") String user) {
        Player player = playerService.findPlayerByName(user);
        if ( player != null ) {
            String description = player.getCurrentLocation().getTransitions().stream()
                    .map(rt -> rt.getDescription()).collect(Collectors.joining(" "));

            MapBodyResourceSupport mapBody = new MapBodyResourceSupport(description);

            return ResponseEntity.ok(mapBody);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * The mapCurrent location transition details.
     * @return Details as text.
     */
    @GetMapping("/creature/current")
    public ResponseEntity<MapBodyResourceSupport> creatures(@RequestHeader(value="X-Expanse-User") String user) {
        Player player = playerService.findPlayerByName(user);
        if ( player != null ) {
            String description = player.getCurrentLocation().getLocationNonPlayers().getNonPlayers().stream()
                    .map(rt -> rt.getDescription()).collect(Collectors.joining(" "));

            MapBodyResourceSupport mapBody = new MapBodyResourceSupport(description);

            return ResponseEntity.ok(mapBody);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * Gets the location map location by id.
     * @param id The location location map id.
     * @return The location location details.
     */
    @GetMapping("/location/{id}")
    public ResponseEntity<Location> location(@RequestHeader(value="X-Expanse-User") String user, @PathVariable(value="id") String id) {
        Player player = playerService.findPlayerByName(user);
        Location location = playerService.findVisitedPlayerLocation(player, id);
        if ( location != null ) {
            return ResponseEntity.ok(location);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("location/items/current")
    public ResponseEntity<MapBodyResourceSupport> items(@RequestHeader(value="X-Expanse-User") String user) {
        Player player = playerService.findPlayerByName(user);
        if ( player != null ) {
            String description = player.getCurrentLocation().getItems().stream()
                    .map(items -> items.getDescription())
                    .collect(Collectors.joining(" ,"));

            MapBodyResourceSupport inventoryBody = new MapBodyResourceSupport(description);

            return ResponseEntity.ok(inventoryBody);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

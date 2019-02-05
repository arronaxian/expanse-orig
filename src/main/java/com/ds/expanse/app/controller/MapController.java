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
        Player player = playerService.findByName(user);
        if ( player != null ) {
            final Location location = player.getCurrentLocation();
            MapBodyResourceSupport mapBody = new MapBodyResourceSupport(location.getDescription());

            // Add links for transition details
            if ( !player.getCurrentLocation().getTransitions().isEmpty() ) {
                mapBody.add(linkTo(methodOn(MapController.class).transitionsCurrent(user)).withRel(CommandResult.Type.detail.toString()));
            }

            // Add links for location items details.
            if ( !player.getCurrentLocation().getItems().isEmpty() ) {
                mapBody.add(linkTo(methodOn(InventoryController.class).mapCurrent(user)).withRel(CommandResult.Type.detail.toString()));
            }

            // Add link for visited location details
            mapBody.add(linkTo(methodOn(MapController.class).visited(user, location.getId())).withRel(CommandResult.Type.location.toString()));

            return ResponseEntity.ok(mapBody);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * The mapCurrent location transition details.
     * @return Details as text.
     */
    @GetMapping("/transitions/current")
    public ResponseEntity<MapBodyResourceSupport> transitionsCurrent(@RequestHeader(value="X-Expanse-User") String user) {
        Player player = playerService.findByName(user);
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
        Player player = playerService.findByName(user);
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
     * Gets the visited map location by id.
     * @param id The visited location map id.
     * @return The visited location details.
     */
    @GetMapping("/visited/{id}")
    public ResponseEntity<Location> visited(@RequestHeader(value="X-Expanse-User") String user, @PathVariable(value="id") String id) {
        Player player = playerService.findByName(user);

        Location location = player.getVisitedLocations().get(id);
        if ( location != null ) {
            return ResponseEntity.ok(location);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

package com.ds.expanse.app.controller;

import com.ds.expanse.app.command.CommandResult;
import com.ds.expanse.app.api.controller.model.Location;
import com.ds.expanse.app.api.controller.model.Player;
import com.ds.expanse.app.controller.resourcesupport.ItemBodyResourceSupport;
import com.ds.expanse.app.controller.resourcesupport.LocationBodyResourceSupport;
import com.ds.expanse.app.controller.resourcesupport.MapBodyResourceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;
import com.ds.expanse.app.api.service.PlayerService;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Map REST controller provides location, transition and item details.
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
    @GetMapping("/location/current")
    public ResponseEntity<LocationBodyResourceSupport> location(@RequestHeader(value="X-Expanse-User") String user) {
        Player player = playerService.findPlayerByName(user);
        if ( player != null ) {
            final Location location = player.getCurrentLocation();
            LocationBodyResourceSupport locationBody = new LocationBodyResourceSupport(location);

            // Add links for transition details if there are any.  Evil trick to have no transitions out.
            if ( !location.getTransitions().isEmpty() ) {
                locationBody.add(linkTo(methodOn(MapController.class).transitions(user)).withRel(CommandResult.Type.transition.toString()));
            }

            // Add links for location items details.
            if ( !location.getItems().isEmpty() ) {
                locationBody.add(linkTo(methodOn(MapController.class).items(user)).withRel(CommandResult.Type.item.toString()));
            }

            return ResponseEntity.ok(locationBody);
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

    @GetMapping("location/items/current")
    public ResponseEntity<ItemBodyResourceSupport> items(@RequestHeader(value="X-Expanse-User") String user) {
        Player player = playerService.findPlayerByName(user);
        if ( player != null ) {
            ItemBodyResourceSupport resourceSupport = new ItemBodyResourceSupport(player.getCurrentLocation().getItems());

            return ResponseEntity.ok(resourceSupport);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * The mapCurrent location transition details.
     * @return Details as text.
     */
    @GetMapping("/location/creature/current")
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


}

package com.ds.expanse.app.controller;

import com.ds.expanse.app.command.CommandResult;
import com.ds.expanse.app.api.loader.Loader;
import com.ds.expanse.app.api.service.PlayerService;
import com.ds.expanse.app.command.ExpanseCommandProcessor;
import com.ds.expanse.app.api.controller.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ds.expanse.app.api.controller.model.Location.Type.market;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(produces = "application/hal+json")
public class CommandController {
    @Autowired
    @Qualifier("PlayerPersistenceService")
    private PlayerService playerService;

    @Autowired
    private Loader loader;

    @Autowired
    private ExpanseCommandProcessor processor;

    @PutMapping(path="/request")
    public ResponseEntity<CommandBodyResourceSupport> requestCommand(@RequestHeader(value="X-Expanse-User") String user, @RequestParam(value="cmd", defaultValue="") String command) {
        // Get the credentials from the header
        Player player = playerService.findByName(user);
        if ( player != null ) {
            // Perform the command
            CommandResult result = processor.applyCommand(player, command);

            // Build the command body
            CommandBodyResourceSupport commandBody = new CommandBodyResourceSupport(result);

            // Save the player's mapCurrent state
            player = playerService.save(player);

            // Add links to the command body
            for ( CommandResult.Type type : result.getTypes()) {
                switch ( type ) {
                    case map:
                        commandBody.add(linkTo(methodOn(MapController.class).current(user)).withRel(CommandResult.Type.detail.toString()));
                        break;
                    case transition:
                        commandBody.add(linkTo(methodOn(MapController.class).transitionsCurrent(user)).withRel(CommandResult.Type.detail.toString()));
                        break;
                    case item:
                    case inventory:
                        commandBody.add(linkTo(methodOn(InventoryController.class).playerCurrent(user)).withRel(type.toString()));
                        break;
                    case creature:
                        commandBody.add(linkTo(methodOn(MapController.class).creatures(user)).withRel(CommandResult.Type.detail.toString()));
                        break;
                }
            }

            // Add links for market items.
            if ( player.getCurrentLocation().getType().equals(market)) {
                commandBody.add(linkTo(methodOn(InventoryController.class).market(user)).withRel(market.toString()));
            }

            return ResponseEntity.ok(commandBody);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

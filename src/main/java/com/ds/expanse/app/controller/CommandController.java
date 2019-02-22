package com.ds.expanse.app.controller;

import com.ds.expanse.app.command.CommandResult;
import com.ds.expanse.app.api.loader.Loader;
import com.ds.expanse.app.api.service.PlayerService;
import com.ds.expanse.app.command.ExpanseCommandProcessor;
import com.ds.expanse.app.api.controller.model.Player;
import com.ds.expanse.app.controller.resourcesupport.CommandBodyResourceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ds.expanse.app.api.controller.model.Location.Type.market;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(value = "/command", produces = "application/hal+json")
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
        Player player = playerService.findPlayerByName(user);
        if ( player != null ) {
            // Perform the command
            CommandResult result = processor.applyCommand(player, command);

            // Build the command body
            CommandBodyResourceSupport commandBody = new CommandBodyResourceSupport(result);

            // Add links to the command body
            for ( CommandResult.Type type : result.getTypes()) {
                switch ( type ) {
                    case location:
                        commandBody.add(linkTo(methodOn(MapController.class).location(user)).withRel(CommandResult.Type.location.toString()));
                        break;
                    case transition:
                        commandBody.add(linkTo(methodOn(MapController.class).transitions(user)).withRel(CommandResult.Type.transition.toString()));
                        break;
                    case item:
                        commandBody.add(linkTo(methodOn(MapController.class).items(user)).withRel(CommandResult.Type.item.toString()));
                        break;
                    case inventory:
                        commandBody.add(linkTo(methodOn(PlayerController.class).inventory(user)).withRel(CommandResult.Type.inventory.toString()));
                        break;
                    case creature:
                        commandBody.add(linkTo(methodOn(MapController.class).creatures(user)).withRel(CommandResult.Type.creature.toString()));
                        break;
                }
            }

            // Add links for market items.
            if ( player.getCurrentLocation().getType().equals(market)) {
                commandBody.add(linkTo(methodOn(MarketController.class).market(user)).withRel(market.toString()));
            }

            return ResponseEntity.ok(commandBody);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

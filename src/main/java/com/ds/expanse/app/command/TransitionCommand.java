package com.ds.expanse.app.command;

import com.ds.expanse.app.api.controller.model.Location;
import com.ds.expanse.app.api.controller.model.LocationTransition;

import java.util.logging.Logger;

public class TransitionCommand extends DefaultCommand {
    private static Logger LOG = Logger.getLogger(TransitionCommand.class.getName());

    private final static String MOVED = "Moved %s to the %s.";
    private final static String UNABLE_TO_MOVE = "Unable to move %s.";

    public TransitionCommand(String command) {
        super(command);
    }

    public CommandResult execute(CommandRequest request) {
        LocationTransition transition =
                request.getPlayer().getCurrentLocation().getTransition(this);

        final CommandResult result = new CommandResult();

        if ( transition != null ) {
            Location toLocation = request.getCurrentLocation(transition.getLocation());

            LOG.info("Transition from '" + request.getPlayer().getCurrentLocation().getName() + "' to " + toLocation.getId() + " > '" + toLocation.getName() + "'");

            request.getPlayer().setCurrentLocation(toLocation);
            result.addResultMessage(MOVED, getCommand(), transition.getLocation().getName());
        } else {
            result.addResultMessage(UNABLE_TO_MOVE, getCommand());
        }

        result.addType(CommandResult.Type.location);
        result.addType(CommandResult.Type.creature);

        if ( request.getPlayer().getCurrentLocation().getType().equals(Location.Type.market)) {
            result.addType(CommandResult.Type.market);
        }

        return result;
    }
}

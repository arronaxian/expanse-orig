package com.ds.expanse.app.command;

import com.ds.expanse.app.api.controller.model.Location;
import com.ds.expanse.app.api.controller.model.LocationTransition;

public class TransitionCommand extends DefaultCommand {
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
            request.getPlayer().setCurrentLocation(transition.getLocation());
            result.addResultMessage(MOVED, getCommand(), transition.getLocation().getName());
        } else {
            result.addResultMessage(UNABLE_TO_MOVE, getCommand());
        }

        result.addType(CommandResult.Type.map);
        result.addType(CommandResult.Type.creature);

        if ( request.getPlayer().getCurrentLocation().getType().equals(Location.Type.market)) {
            result.addType(CommandResult.Type.market);
        }

        return result;
    }
}

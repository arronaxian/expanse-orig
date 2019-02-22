package com.ds.expanse.app.command;

import com.ds.expanse.app.api.controller.model.Location;

public class LookAtRoomCommand extends DefaultCommand {

    public LookAtRoomCommand(String command) {
        super(command);
    }

    public CommandResult execute(CommandRequest request) {
        final CommandResult result = new CommandResult();

        Location location = request.getPlayer().getCurrentLocation();

        result.addResultMessage("%s - %s", location.getName(), location.getDescription());

        location.getTransitions().forEach(t -> {
            result.addResultMessage("%s ", t.getDescription() );
        });

        location.getItems().forEach(i -> {
            result.addResultMessage("%s ", i.getDescription() );
        });

        result.addType(CommandResult.Type.location);
        result.addType(CommandResult.Type.item);

        return result;
    }
}

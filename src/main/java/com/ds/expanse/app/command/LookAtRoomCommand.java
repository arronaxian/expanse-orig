package com.ds.expanse.app.command;

public class LookAtRoomCommand extends DefaultCommand {

    public LookAtRoomCommand(String command) {
        super(command);
    }

    public CommandResult execute(CommandRequest request) {
        final CommandResult result = new CommandResult();

        result.addType(CommandResult.Type.map);
  //      result.addType(CommandResult.Type.item);
  //      result.addType(CommandResult.Type.creature);

        return result;
    }
}

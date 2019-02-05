package com.ds.expanse.app.command;

/**
 * The request cannot be matched to a known command.
 */
public class UnknownCommand extends DefaultCommand {
    public UnknownCommand(String command) {
        super(command);
    }

    public CommandResult execute(CommandRequest request) {
        CommandResult response = new CommandResult();
        response.addResultMessage("I do not understand the request.");

        return response;
    }
}

package com.ds.expanse.app.command;

import java.util.List;

public class SaveCommand extends DefaultCommand  {
    public SaveCommand(String command) {
        super(command);
    }

    public CommandResult execute(CommandRequest request) {
        // save name emailaddress
        // save

        List<String> tokens = tokenizer(request.getUserCommand());

        final CommandResult result = new CommandResult();
        if ( tokens.size() == 3 ) {
            String name = tokens.get(1);
            String emailAddress = tokens.get(2);


            try {
                request.getProcessor().savePlayer(request.getPlayer());
                result.addResultMessage("Player " + request.getPlayer().getName() + " is saved.");
            } catch (Exception e) {
            }
        } else {
            result.addResultMessage("");
        }

        return result;
    }
}

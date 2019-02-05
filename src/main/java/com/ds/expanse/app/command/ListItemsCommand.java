package com.ds.expanse.app.command;

public class ListItemsCommand extends DefaultCommand {
    public ListItemsCommand(String command) {
        super(command);
    }

    public CommandResult execute(CommandRequest context) {
        CommandResult result = new CommandResult();

        int itemCount = context.getPlayer().getItemList().size();
        if ( itemCount == 0 ) {
            result.addResultMessage("You have no items.");
        } else if ( itemCount == 1 ) {
            result.addResultMessage("You have 1 item.");
        } else {
            result.addResultMessage("You have " + itemCount + " items.");
        }

        result.addType(CommandResult.Type.item);

        return result;
    }
}

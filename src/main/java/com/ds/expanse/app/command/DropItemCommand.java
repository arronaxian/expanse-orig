package com.ds.expanse.app.command;

import com.ds.expanse.app.api.controller.model.Item;
import com.ds.expanse.app.api.controller.model.Player;

public class DropItemCommand extends DefaultCommand {
    public DropItemCommand(String command) {
        super(command);
    }

    public CommandResult execute(CommandRequest request) {
        String itemName = tokenizer(request.getUserCommand()).get(1);

        CommandResult result = new CommandResult();

        Item item = request.getPlayer().getItemByName(itemName);
        if ( item == null ) {
            result.addResultMessage("You do not have " + itemName + " to drop.");
        } else if ( item.getCount() > 1 ) {
            dropItem(request.getPlayer(), itemName);

            result.addResultMessage(item.getName());
            result.addResultMessage( " dropped.");
        } else if ( !item.isEquipped() && item.getCount() == 1 ) {
            dropItem(request.getPlayer(), item.getName());

            result.addResultMessage(item.getName());
            result.addResultMessage( " dropped.");
        } else if ( item.isEquipped() ) {
            result.addResultMessage(item.getName());
            result.addResultMessage( " is equipped and cannot be dropped.");
        }

        result.addType(CommandResult.Type.inventory);

        return result;
    }

    public Item dropItem(Player player, String itemName) {
        Item item = player.removeItem(itemName);
        player.getCurrentLocation().addItem(item);

        return item;
    }
}

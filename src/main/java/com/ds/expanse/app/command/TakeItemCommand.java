package com.ds.expanse.app.command;

import com.ds.expanse.app.api.controller.model.Item;
import com.ds.expanse.app.api.controller.model.Location;

public class TakeItemCommand extends DefaultCommand {
    public TakeItemCommand(String command) {
        super(command);
    }

    public CommandResult execute(CommandRequest request) {
        CommandResult result = new CommandResult();

        String itemName = tokenizer(request.getUserCommand()).get(1);

        Location currentLocation = request.getPlayer().getCurrentLocation();


        if ( !request.getPlayer().findItemByName(itemName)) {
            result.addResultMessage("There is no " + itemName + " here.");
        } else {
            // Take the item from the current location and add it to the player.
            Item item = currentLocation.removeItem(itemName);
            currentLocation.setAltered(true);
            request.getPlayer().addItem(item);

            result.addResultMessage("You have the " + item.getName());
        }



        result.addType(CommandResult.Type.inventory);

        return result;
    }
}

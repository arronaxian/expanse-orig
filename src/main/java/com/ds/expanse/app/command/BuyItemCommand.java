package com.ds.expanse.app.command;

import com.ds.expanse.app.api.controller.model.Location;

import static com.ds.expanse.app.api.controller.model.Market.*;

public class BuyItemCommand extends DefaultCommand {
    public BuyItemCommand(String command) {
        super(command);
    }

    public CommandResult execute(CommandRequest request) {
        CommandResult result = new CommandResult();

        String itemName = tokenizer(request.getUserCommand()).get(1);

        Location currentLocation = request.getPlayer().getCurrentLocation();
        if ( !Location.Type.market.equals(currentLocation.getType() ) ) {
            result.addResultMessage("There is nothing to buy here.");
        } else {
            final Purchase purchase = request.getProcessor().getMarket().buyItem(request.getPlayer(), itemName);
            switch ( purchase ) {
                case nosuchitem:
                    result.addResultMessage("I do not sell " + itemName + ".");
                    break;
                case sale:
                    result.addResultMessage("You purchased the " + itemName + ".");
                    break;
                case insufficentfunds:
                    result.addResultMessage("You don't have enough to buy " + itemName + ".");
                    break;
            }
        }

        result.addType(CommandResult.Type.inventory);

        return result;
    }
}

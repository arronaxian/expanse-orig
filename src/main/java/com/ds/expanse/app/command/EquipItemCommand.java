package com.ds.expanse.app.command;

import com.ds.expanse.app.api.controller.model.Item;

/**
 * equip <item> as primary
 */
public class EquipItemCommand extends DefaultCommand {
    public EquipItemCommand(String command) {
        super(command);
    }

    public CommandResult execute(CommandRequest request) {
        String itemName = tokenizer(request.getUserCommand()).get(1);
//        String equipAs = tokenizer(request.getUserCommand()).get(2);

        CommandResult result = new CommandResult();

        Item item = request.getPlayer().getItemByName(itemName);

        if ( item == null ) {
            result.addResultMessage("You don't have an " + itemName + " to equip.");
        } else if ( item.getType() == Item.Type.tool || item.getType() == Item.Type.weapon ) {
            result.addResultMessage("The " + item.getName() + " is equipped.");

            request.getPlayer().getEquippedPrimaryWeapon().setEquipped(false);
            request.getPlayer().setEquippedPrimaryWeapon(item);
        } else {
            result.addResultMessage("You cannot equip " + item.getName() + ".");
        }

        result.addType(CommandResult.Type.inventory);

        return result;
    }
}

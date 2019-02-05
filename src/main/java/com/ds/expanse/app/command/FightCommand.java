package com.ds.expanse.app.command;

import com.ds.expanse.app.api.controller.model.Item;
import com.ds.expanse.app.api.controller.model.Location;
import com.ds.expanse.app.api.controller.model.NonPlayer;
import com.ds.expanse.app.api.controller.model.Player;

import java.util.List;

/**
 * Fight commands
 */
public class FightCommand extends DefaultCommand {
    public FightCommand(String command) {
        super(command);
    }

    public CommandResult execute(CommandRequest request) {
        String nonPlayerName = tokenizer(request.getUserCommand()).get(1);

        CommandResult result = new CommandResult();

        final Player player = request.getPlayer();
        final List<NonPlayer> nonPlayers = player.getCurrentLocation().getLocationNonPlayers().getNonPlayers();
        nonPlayers.stream()
            .filter(f -> f.getName().equalsIgnoreCase(nonPlayerName))
            .forEach(np -> {
                if ( firstBlow(result, player, np) ) {
                    offensePlayer(result, player, np);
                    offenseNonPlayer(result, player, np);
                } else {
                    offenseNonPlayer(result, player, np);
                    offensePlayer(result, player, np);
                }

                result.addResultMessage(" (" + np.getHealth() + ").");
        });

        return result;
    }

    protected void dropItem(CommandResult result, NonPlayer nonPlayer, Location location) {
        nonPlayer.getItemList().forEach(i -> {
            Item item = nonPlayer.removeItem(i.getName());
            location.addItem(item);

            result.addResultMessage("There are " + item.getName() + ".");
        });
    }

    /**
     * Player offense attack multiple NonPlayers
     * @param result
     * @param player
     * @param nonPlayer
     */
    protected void offensePlayer(CommandResult result, Player player, NonPlayer nonPlayer) {
        if ( !player.isDead() ) {
            Item item = player.getEquippedPrimaryWeapon();

            int hitDamage = hitDamage(item);

            nonPlayer.adjustHealth(-hitDamage);

            if ( hitDamage == 0 ) {
                result.addResultMessage("You missed the " + nonPlayer.getName() + " with your " + player.getEquippedPrimaryWeapon().getName() + ".");
            } else {
                result.addResultMessage("You hit the " + nonPlayer.getName() + " with your " + player.getEquippedPrimaryWeapon().getName() + ".");
            }
        }
    }

    protected void offenseNonPlayer(CommandResult result, Player player, NonPlayer nonPlayer) {
        if ( !nonPlayer.isDead() ) {
            Item item = nonPlayer.getEquippedPrimaryWeapon();

            int hitDamage = hitDamage(item);

            player.adjustHealth(-hitDamage);

            if ( hitDamage == 0 ) {
                result.addResultMessage("You dodged the attack of the " + nonPlayer.getName() + ".");
            } else {
                result.addResultMessage("You were bitten by the " + nonPlayer.getName() + ".");
            }
        }

        if ( nonPlayer.isDead() ){
            result.addResultMessage("You killed the " + nonPlayer.getName() );

            dropItem(result, nonPlayer, player.getCurrentLocation());

            result.addType(CommandResult.Type.item);
        }
    }

    protected boolean firstBlow(CommandResult result, Player player, NonPlayer nonPlayer) {
        return true;
    }

    protected int hitDamage(Item item) {
        return (int)Math.round(Math.random() * item.getHitDamage());
    }
}

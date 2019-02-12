package com.ds.expanse.app.api.command;

import com.ds.expanse.app.api.controller.model.Market;
import com.ds.expanse.app.command.CommandResult;
import com.ds.expanse.app.api.controller.model.Player;

public interface CommandProcessor {
    CommandResult applyCommand(Player player, String command);

    Player findPlayerByName(String playerName);
    Player savePlayer(Player player);
    Market getMarket();

    /**
     * Saves the player location changed.
     * @param player
     * @return
     */
    Player savePlayerLocationChanged(Player player);
}

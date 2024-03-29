package com.ds.expanse.app.api.command;

import com.ds.expanse.app.api.controller.model.Location;
import com.ds.expanse.app.api.controller.model.Market;
import com.ds.expanse.app.command.CommandResult;
import com.ds.expanse.app.api.controller.model.Player;

public interface CommandProcessor {
    CommandResult applyCommand(Player player, String command);

    Player findPlayerByName(String playerName);
    Player savePlayer(Player player);

    /**
     * Gets the current location for the player.
     * @param player The player to get the current location for.
     * @return The current location for the player.
     */
    Location getCurrentLocation(Player player, Location toLocation);

    Market getMarket();
}

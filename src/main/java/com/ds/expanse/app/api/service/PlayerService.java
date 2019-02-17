package com.ds.expanse.app.api.service;

import com.ds.expanse.app.api.controller.model.Location;
import com.ds.expanse.app.api.controller.model.Player;

public interface PlayerService {
    /**
     * Creates a temporary player.
     * @return The temporary player instance.
     */
    Player createTemporaryPlayer();

    /**
     * Saves the player instance.
     * @param player The player instance to be saved.
     * @return The saved player instance.
     */
    Player save(Player player);

    /**
     * Finds the player by name.
     * @param name The name of the player.
     * @return A player, otherwise null if not found.
     */
    Player findPlayerByName(String name);

    /**
     * TODO: remove this and let findVisitedPlayerLocation do the work.
     * Finds the player's altered location.
     * @param player The player.
     * @return The current location instance of the player, which must be non-null.
     */
    Location findAlteredPlayerLocation(Player player, Location toLocation);

    /**
     * Finds the player's visited location.
     * @param player The player instance.
     * @param locationId The locationId
     * @return Returns the location, otherwise null.
     */
    Location findVisitedPlayerLocation(Player player, String locationId);


}

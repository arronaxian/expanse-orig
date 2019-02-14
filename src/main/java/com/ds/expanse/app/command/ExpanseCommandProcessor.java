package com.ds.expanse.app.command;

import com.ds.expanse.app.api.command.Command;
import com.ds.expanse.app.api.command.CommandProcessor;
import com.ds.expanse.app.api.controller.model.Location;
import com.ds.expanse.app.api.controller.model.Market;
import com.ds.expanse.app.api.service.PlayerService;
import com.ds.expanse.app.loader.ExpanseLoader;
import com.ds.expanse.app.nlp.ExpanseNLP;
import com.ds.expanse.app.api.controller.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Processes commands sent by the Player.
 */
@Service(value="CommandProcessor")
public class ExpanseCommandProcessor implements CommandProcessor {
    @Autowired
    @Qualifier("PlayerPersistenceService")
    PlayerService playerService;

    @Autowired
    protected ExpanseLoader loader;

    public ExpanseCommandProcessor() {
    }

    public CommandResult applyCommand(Player player, String command) {
        final ExpanseNLP nlp = loader.getNLP();

        Command lookedUpCommand = nlp.matchCommand(command);
        CommandResult result = (CommandResult)lookedUpCommand.execute(new CommandRequest(command, player, this));

        // Save the player's mapCurrent state
        player = playerService.save(player);

        return result;
    }

    @Override
    public Player findPlayerByName(String playerName) {
        return playerService.findPlayerByName(playerName);
    }

    @Override
    public Player savePlayer(Player player) {
        return playerService.save(player);
    }

    @Override
    public Location getCurrentLocation(Player player, Location toLocation) {
        return playerService.findAlteredPlayerLocation(player, toLocation);
    }

    @Override
    public Market getMarket() {
        return loader.getMarket();
    }
}

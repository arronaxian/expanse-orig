package com.ds.expanse.app.command;

import com.ds.expanse.app.api.command.CommandProcessor;
import com.ds.expanse.app.api.controller.model.Market;
import com.ds.expanse.app.api.controller.model.Player;
import lombok.Getter;

public class CommandRequest {
    private @Getter final Player player;
    private @Getter String userCommand;

    private final CommandProcessor processor;

    public CommandRequest(String userCommand, Player player, CommandProcessor processor) {
        this.player = player;
        this.userCommand = userCommand;

        this.processor = processor;
    }

    public Market getMarket() {
        return this.processor.getMarket();
    }

    public void locationChanged() {
        // this.process.savePlayerLocationChanged(player);
    }

    public void savePlayer() {
        this.processor.savePlayer(player);
    }
}

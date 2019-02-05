package com.ds.expanse.app.command;

import com.ds.expanse.app.api.command.CommandProcessor;
import com.ds.expanse.app.api.controller.model.Market;
import com.ds.expanse.app.api.controller.model.Player;

public class CommandRequest {
    private final Player player;
    private String userCommand;
    private CommandProcessor processor;

    public CommandRequest(String userCommand, Player player, CommandProcessor processor) {
        this.player = player;
        this.setUserCommand(userCommand);

        this.processor = processor;
    }

    public CommandProcessor getProcessor() {
        return processor;
    }

    public Player getPlayer() {
        return player;
    }

    public String getUserCommand() {
        return userCommand;
    }

    public void setUserCommand(String userCommand) {
        this.userCommand = userCommand;
    }
}

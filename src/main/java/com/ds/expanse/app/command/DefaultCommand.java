package com.ds.expanse.app.command;

import com.ds.expanse.app.api.command.Command;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Default command
 */
public class DefaultCommand implements Command<CommandRequest, CommandResult> {
    @Getter protected String command;

    public DefaultCommand(String command) {
        if ( command == null ) {
            command = "";
        }

        this.command = command;
    }

    @Override
    public CommandResult execute(CommandRequest context) {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if ( obj == null ) {
            return false;
        }
        return command.equalsIgnoreCase(obj.toString());
    }

    public int hashCode() {
        return command.hashCode();
    }

    @Override
    public String toString() {
        return command;
    }

    protected boolean compare(String command) {
        List<String> defaultCommandTokens = tokenizer(this.command);
        List<String> commandTokens = tokenizer(command);

        return defaultCommandTokens.equals(commandTokens);
    }

    /**
     * Simple command tokenizer on space.
     * @param command The command to tokenize.
     * @return
     */
    protected List<String> tokenizer(String command) {
        String[] splits = command.split(" ");

        return Stream.of(splits)
                .map(c -> c.trim().toLowerCase()).collect(Collectors.toList());
    }
}

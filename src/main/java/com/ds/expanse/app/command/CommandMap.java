package com.ds.expanse.app.command;

import com.ds.expanse.app.api.command.Command;

import java.util.*;

/**
 * Maps a Command to an Object of type M.
 * @param <M> The type of object to MapElements.
 */
public class CommandMap<M> {
    protected final Map<Command, M> commandMap = new HashMap<>();

    public M get(Command command) {
        return commandMap.get(command);
    }

    public boolean hasCommand(Command command) {
        return commandMap.containsKey(command);
    }

    public void put(Command command, M obj) {
        commandMap.put(command, obj);
    }

    public M remove(Command command) {
        return commandMap.remove(command);
    }

    public Collection<M> values() {
        return commandMap.values();
    }

    public Set<Map.Entry<Command, M>> entrySet() {
        return commandMap.entrySet();
    }

    public String toString() {
        return commandMap.toString();
    }
}

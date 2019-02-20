package com.ds.expanse.app.api.controller.model;

import com.ds.expanse.app.api.command.Command;
import com.ds.expanse.app.command.CommandMap;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The list of navigable transitions from the location.
 */
public class LocationTransitions {
    @Getter @Setter private CommandMap<LocationTransition> transitionCommands = new CommandMap<>();

    public List<LocationTransition> getTransitions() {
        return new ArrayList<>( transitionCommands.values() );
    }

    @JsonIgnore
    public LocationTransition getTransition(Command command) {
        return transitionCommands.get(command);
    }

    public void addTransition(Command command, LocationTransition locationTransition) {
        transitionCommands.put(command, locationTransition);
    }
}

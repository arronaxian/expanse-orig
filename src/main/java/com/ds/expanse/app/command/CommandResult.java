package com.ds.expanse.app.command;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class CommandResult {
    public enum Type { detail, map, item, inventory, locationitem, location, transition, visited, market, creature};

    @Getter protected StringBuilder messages = new StringBuilder();
    @Getter protected List<Type> types = new ArrayList<>();

    public CommandResult() {
    }

    public void addResultMessage(String responseMessageTemplate, Object ... values) {
        messages.append(String.format(responseMessageTemplate, values));
    }

    public void addResultMessage(String responseMessage) {
        messages.append(responseMessage);
    }

    public void addType(Type type) {
        types.add(type);
    }

    public String toString() {
        return messages.toString();
    }
}

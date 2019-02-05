package com.ds.expanse.app.api.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * Associates a command with an item.
 */
public class ItemCommand {
    public ItemCommand() {
    }

    @Getter  @Setter private String id;
    @Getter @Setter private Item item;
    @Getter @Setter private String description;
    @Getter @Setter private String command;
}

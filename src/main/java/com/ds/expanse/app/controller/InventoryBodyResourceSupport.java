package com.ds.expanse.app.controller;

import com.ds.expanse.app.command.CommandResult;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

public class InventoryBodyResourceSupport extends ResourceSupport {
    @Getter @Setter private String result;

    public InventoryBodyResourceSupport(String result) {
        this.result = result.toString();
    }
}

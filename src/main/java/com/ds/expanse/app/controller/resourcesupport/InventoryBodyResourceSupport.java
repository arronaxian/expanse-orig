package com.ds.expanse.app.controller.resourcesupport;

import com.ds.expanse.app.api.controller.model.Item;
import com.ds.expanse.app.command.CommandResult;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

import java.util.Collection;

public class InventoryBodyResourceSupport extends ResourceSupport {
    @Getter @Setter private Collection<Item> result;

    public InventoryBodyResourceSupport(Collection<Item> inventory) {
        this.result = inventory;
    }
}

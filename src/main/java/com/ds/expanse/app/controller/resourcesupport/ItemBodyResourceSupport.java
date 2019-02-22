package com.ds.expanse.app.controller.resourcesupport;

import com.ds.expanse.app.api.controller.model.Item;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

import java.util.Collection;
import java.util.List;

public class ItemBodyResourceSupport extends ResourceSupport {
    @Getter @Setter private Collection<Item> result;

    public ItemBodyResourceSupport(Collection<Item> items) {
        this.result = items;
    }
}

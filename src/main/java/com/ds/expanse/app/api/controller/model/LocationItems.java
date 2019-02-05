package com.ds.expanse.app.api.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Items at the location.
 */
public class LocationItems {
    private Location parent;

    private Map<String, Item> items = new HashMap<>();

    public LocationItems(Location parent) {
        this.parent = parent;
    }

    @JsonIgnore
    public Location getParentLocation() {
        return parent;
    }

    public void addItem(Item item) {
        items.put(item.getName(), item);
    }

    public Item removeItem(String item) {
        return items.remove(item);
    }

    public Item removeItem(Item item) {
        return items.remove(item.getName());
    }

    public Collection<Item> getItems() {
        return items.values();
    }
}

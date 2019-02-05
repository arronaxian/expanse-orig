package com.ds.expanse.app.api.controller.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Inventory items is a map backed items.
 */
public class InventoryItems {
    /**
     * Maps item name to the item instance.
     */
    protected final Map<String, Item> items = new HashMap<>();

    /**
     * Add an item.
     * @param item The item to add.
     */
    public void addItem(Item item) {
        Item hasItem = items.get(item.getName());
        if ( hasItem != null ) {
            hasItem.setCount(hasItem.getCount()+1);
        } else {
            hasItem = item.clone();
            hasItem.setCount(1);
        }
        items.put(hasItem.getName(), hasItem);
    }

    /**
     * Remove the item.
     * @param item The item to remove.
     * @return The removed item.
     */
    public Item removeItem(Item item) {
        return removeItem(item.getName());
    }

    /**
     * Remove the item.
     * @param itemName The item name to remove.
     * @return The removed item.
     */
    public Item removeItem(String itemName) {
        Item hasItem = items.get(itemName);
        if ( hasItem == null ) {
            return null;
        } else {
            if ( hasItem.getCount() > 1 ) {
                hasItem.setCount(hasItem.getCount() - 1);
            } else {
                hasItem = items.remove(hasItem.getName());
            }
        }
        return hasItem;
    }

    public Item getItemByName(String itemName) {
        return items.get(itemName);
    }

    /**
     * Gets the items as a collection.
     * @return The items.
     */
    public List<Item> getItems() {
        return items.values().stream().collect(Collectors.toList());
    }
}

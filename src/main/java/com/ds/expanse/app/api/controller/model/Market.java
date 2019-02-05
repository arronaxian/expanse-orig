package com.ds.expanse.app.api.controller.model;

import java.util.Collection;
import java.util.Map;

public class Market {
    public enum Purchase { insufficentfunds, nosuchitem, sale, }

    private Map<String, Item> items;

    public Market(Map<String, Item> items) {
        this.items = items;
    }

    public Collection<Item> getItems(Player player) {
        return items.values();
    }

    public Purchase buyItem(Player player, String itemName) {
        Item item = items.get(itemName);
        if ( item == null ) {
            return Purchase.nosuchitem;
        } else {
            if ( player.getCoins() < item.getCost() ) {
                return Purchase.insufficentfunds;
            } else {
                player.setCoins(player.getCoins() - item.getCost());

                player.addItem(item);
            }

            return Purchase.sale;
        }

    }

}

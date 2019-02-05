package com.ds.expanse.app.api.loader.model;

import java.util.ArrayList;
import java.util.List;


public class ItemsDO {
    List<ItemDO> items = new ArrayList<>();

    public ItemsDO() {
    }

    public List<ItemDO> getItems() {
        return items;
    }

    public void setItems(List<ItemDO> items) {
        this.items = items;
    }
}

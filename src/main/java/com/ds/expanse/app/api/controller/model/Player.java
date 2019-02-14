package com.ds.expanse.app.api.controller.model;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class Player {
    public enum Type { player, beast}

    @Getter @Setter protected String id;
    @Getter @Setter protected String name;
    @Getter @Setter protected Location currentLocation;
    @Getter @Setter protected int coins = 125;
    @Getter @Setter protected Type type = Type.player;
    @Getter @Setter protected int health = 100;

    protected Item equippedPrimaryWeapon;

    public Player(String name) {
        this.name = name;
    }

    protected InventoryItems items = new InventoryItems();

    public Collection<Item> getItemList() {
        return items.getItems();
    }

    public Item getItemByName(String itemName) {
        return items.getItemByName(itemName);
    }

    public boolean hasItem(String itenName) {
        return items.getItemByName(itenName) != null;
    }

    public void addItem(Item item) {
        items.addItem(item);
    }

    public Item removeItem(String itemName) {
        return items.removeItem(itemName);
    }

    public boolean hasItems() {
        return !items.getItems().isEmpty();
    }

    public void adjustHealth(int amount) {
        health += amount;
        health = Math.max(0, health);
    }

    public boolean isDead() {
        return health <= 0;
    }

    public Item getEquippedPrimaryWeapon() {
        if ( equippedPrimaryWeapon == null ) {
            equippedPrimaryWeapon = new Item();
            equippedPrimaryWeapon.setHitDamage(2);
            equippedPrimaryWeapon.setName("hands");
            equippedPrimaryWeapon.setCriticalHitDamage(0);
            equippedPrimaryWeapon.setCriticalHitChance(0);
        }

        return equippedPrimaryWeapon;
    }

    public void setEquippedPrimaryWeapon(Item primaryWeapon) {
        this.equippedPrimaryWeapon = primaryWeapon;
        this.equippedPrimaryWeapon.setEquipped(true);
    }

    public boolean isNew() {
        return id == null;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        try {
            return ((Player)obj).getId().equalsIgnoreCase(id);
        } catch ( Exception e ) {
            return false;
        }
    }
}

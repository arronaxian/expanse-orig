package com.ds.expanse.app.api.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.hateoas.Identifiable;

import java.util.Objects;

/**
 * An item that can be interacted.
 */
public class Item implements Identifiable<String> {
    public enum Type { artifact, tool, weapon, provision }

    @Id
    protected String id;
    @Getter @Setter protected String name;
    @Getter @Setter protected String description;
    @Getter @Setter protected Type type = Type.artifact;
    @Getter @Setter protected int cost = 1;
    @Getter @Setter protected int count = 1;
    @Getter @Setter protected int hitDamage;
    @Getter @Setter protected int criticalHitDamage;
    @Getter @Setter protected int criticalHitChance;
    @Getter @Setter protected boolean isEquipped = false;

    public Item() {
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonIgnore
    public boolean isNew() {
        return id == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(name, item.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return description;
    }

    @Override
    public Item clone() {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setDescription(description);
        item.setType(type);
        item.setCost(cost);
        item.setCount(count);
        item.setCriticalHitChance(criticalHitChance);
        item.setCriticalHitDamage(criticalHitDamage);
        item.setHitDamage(hitDamage);

        return item;
    }
}

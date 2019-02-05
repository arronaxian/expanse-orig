package com.ds.expanse.app.api.loader.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "items")
public class ItemDO {
    @Id @Getter @Setter private String id;
    @Getter @Setter private String name;
    @Getter @Setter private String description;
    @Getter @Setter private String details;
    @Getter @Setter private String type;
    @Getter @Setter private int cost;
    @Getter @Setter private int count;
    @Getter @Setter private int criticalHitDamage = 0;
    @Getter @Setter private int criticalHitChance = 0;
    @Getter @Setter private int hitDamage = 0;

    public ItemDO() {
    }
}

package com.ds.expanse.app.api.loader.model;

import lombok.Getter;
import lombok.Setter;

public class NonPlayerDO {
    @Getter @Setter private String id;
    @Getter @Setter private String name;
    @Getter @Setter private String description;
    @Getter @Setter private int health;
    @Getter @Setter private String type;
    @Getter @Setter private String drop;
    @Getter @Setter private int hit = 0;
}

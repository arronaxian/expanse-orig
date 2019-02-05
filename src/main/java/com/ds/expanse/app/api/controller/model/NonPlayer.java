package com.ds.expanse.app.api.controller.model;

import lombok.Getter;
import lombok.Setter;

public class NonPlayer extends Player {
    @Getter @Setter private String description;

    public NonPlayer(String name) {
        super(name);
    }
}

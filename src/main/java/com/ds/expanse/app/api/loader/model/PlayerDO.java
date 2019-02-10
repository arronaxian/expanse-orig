package com.ds.expanse.app.api.loader.model;

import com.ds.expanse.app.api.controller.model.Location;
import com.ds.expanse.app.api.controller.model.Player;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "players")
public class PlayerDO {
    public PlayerDO() {
    }

    @Id @Getter @Setter protected String id;
    @Getter @Setter protected String name;
    @Getter @Setter protected String emailAddress;
    @Getter @Setter protected String uid;
    @Getter @Setter protected int coins;
    @Getter @Setter protected int health;
    @DBRef @Setter @Getter protected LocationDO currentLocation;
    @DBRef @Getter @Setter List<ItemDO> items = new ArrayList<>();

    public boolean isNew() {
        return id == null;
    }
}

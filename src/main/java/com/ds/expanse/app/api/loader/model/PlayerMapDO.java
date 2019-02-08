package com.ds.expanse.app.api.loader.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Keeps track of the relationship between the player, the origin (unmodified) location and
 * the players visited location, which may be modified.
 */
@Document(collection = "playermaps")
public class PlayerMapDO {
    public void PlayerMapDO() {
    }

    // TODO: could this be done with inheritance?
    @Id @Setter @Getter private String id;
    @DBRef @Setter @Getter private LocationDO visitedLocation;
    @DBRef @Setter @Getter private PlayerDO player;
}

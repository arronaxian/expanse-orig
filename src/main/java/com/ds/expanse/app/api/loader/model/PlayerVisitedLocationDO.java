package com.ds.expanse.app.api.loader.model;

import com.ds.expanse.app.api.controller.model.Player;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * The player's visited locations.  This is book keeping.  References exist for each visited location by player.
 */
@Document(collection = "playervisitedlocation")
public class PlayerVisitedLocationDO {
    @Id @Getter @Setter String id;
    @DBRef @Getter @Setter PlayerDO player;
    @DBRef @Getter @Setter LocationDO location;

    public PlayerVisitedLocationDO() {
    }
}

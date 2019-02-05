package com.ds.expanse.app.api.loader.model;

import com.ds.expanse.app.api.controller.model.Location;
import com.ds.expanse.app.api.controller.model.Player;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "playermaps")
public class PlayerMapDO {
    public void PlayerMapDO() {
    }

    @DBRef @Setter @Getter private Location location;
    @DBRef @Setter @Getter private Location modifiedLocation;
    @DBRef @Setter @Getter private Player player;
}

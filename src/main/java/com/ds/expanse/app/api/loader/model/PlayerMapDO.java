package com.ds.expanse.app.api.loader.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "playermaps")
public class PlayerMapDO {
    public void PlayerMapDO() {
    }

    @DBRef @Setter @Getter private LocationDO location;
    @DBRef @Setter @Getter private LocationDO visistedLocations;
    @DBRef @Setter @Getter private PlayerDO player;
}

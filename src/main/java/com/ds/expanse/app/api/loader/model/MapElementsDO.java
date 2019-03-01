package com.ds.expanse.app.api.loader.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Map elements tracks all the map data needed for the game.
 */
@Document(collection = "mapelements")
public class MapElementsDO {
    @Id @Getter @Setter private String id;
    @Getter @Setter private String name;
    @DBRef @Getter @Setter private LocationDO startLocation;
    @Getter @Setter transient private List<LocationDO> locations = new ArrayList<>();

    public MapElementsDO() {
    }
}

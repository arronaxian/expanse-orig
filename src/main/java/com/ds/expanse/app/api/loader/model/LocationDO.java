package com.ds.expanse.app.api.loader.model;

import com.ds.expanse.app.api.controller.model.Item;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection="locations")
public class LocationDO {
    @Id @Getter @Setter private String id;
    @Getter @Setter private String name;
    @Getter @Setter private String description;
    @Getter @Setter private String type;
    @Getter @Setter private int mapx;
    @Getter @Setter private int mapy;

    @DBRef @Getter @Setter private List<LocationTransitionDO> locationTransitions = new ArrayList<>();
    @DBRef @Getter @Setter private List<ItemDO> items = new ArrayList<>();

    public LocationDO() {
    }

    public boolean hasLocationTransitions() {
        return locationTransitions != null && !locationTransitions.isEmpty();
    }

    public boolean hasItems() {
        return items != null && !items.isEmpty();
    }

    public boolean equals(Object object) {
        try {
            return getId().equalsIgnoreCase(((LocationDO)object).getId());
        } catch ( Exception e ) {
            return false;
        }
    }

    public int hashCode() {
        return getId().hashCode();
    }
}

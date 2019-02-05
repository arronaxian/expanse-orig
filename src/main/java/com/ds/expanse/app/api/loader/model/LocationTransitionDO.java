package com.ds.expanse.app.api.loader.model;

import com.ds.expanse.app.api.controller.model.Location;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "locationtransitions")
public class LocationTransitionDO {
    @Getter @Setter private String id;
    @Getter @Setter private String transition;
    @Getter @Setter private String description;
    @DBRef @Getter @Setter private LocationDO location;

    public LocationTransitionDO() {
    }

    @Override
    public boolean equals(Object object) {
        try {
            return getId().equalsIgnoreCase(((LocationTransitionDO)object).getId());
        } catch ( Exception e ) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}

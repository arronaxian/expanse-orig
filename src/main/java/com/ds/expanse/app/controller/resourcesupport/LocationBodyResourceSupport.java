package com.ds.expanse.app.controller.resourcesupport;

import com.ds.expanse.app.api.controller.model.Location;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

public class LocationBodyResourceSupport extends ResourceSupport {
    @Getter @Setter private Location result;

    public LocationBodyResourceSupport(Location location) {
        this.result = location;
    }
}

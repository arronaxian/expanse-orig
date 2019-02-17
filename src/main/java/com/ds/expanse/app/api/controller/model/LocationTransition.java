package com.ds.expanse.app.api.controller.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Identifiable;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The transitionsCurrent from the location.  Transitions lead away (out of) the mapCurrent location.
 */
public class LocationTransition implements Identifiable<String> {
    private String id;
    @Getter @Setter private String description;

    // The input to change to a new location
    @Getter @Setter private String transition;

    private Location toLocation;

    public LocationTransition() {
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocationTransition(Location locationA) {
        this.toLocation = locationA;
    }

    public LocationTransition(String description, String id) {
        this.toLocation = new Location(id);
    }

    public Location getLocation() {
        return toLocation;
    }

    public void setLocation(Location location) {
        this.toLocation = location;
    }

    @Override
    public boolean equals(Object object) {
        try {
            return getId().equalsIgnoreCase(((LocationTransition)object).getId());
        } catch ( Exception e ) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    public String toString() {
        return " locationtransition id:" + id + " toLocationId:" + toLocation.getId() + " name: " + toLocation.getName();
    }
}
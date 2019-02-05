package com.ds.expanse.app.api.controller.model;

import java.util.ArrayList;
import java.util.List;

/**
 * MapElements object contains all visualized spatial relationships.
 */
public class MapElements {
    List<Location> locations = new ArrayList<>();

    public MapElements() {
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}

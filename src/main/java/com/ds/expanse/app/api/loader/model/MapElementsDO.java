package com.ds.expanse.app.api.loader.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Map elements tracks all the map data needed for the game.
 */
public class MapElementsDO {
    // Locations found on the map
    @Getter @Setter private List<LocationDO> locations = new ArrayList<>();

    public MapElementsDO() {
    }
}

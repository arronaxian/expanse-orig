package com.ds.expanse.app.api.controller.model;

import com.ds.expanse.app.api.command.Command;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Identifiable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Navigable locations on the map.
 */
public class Location implements Identifiable<String> {
    public enum Type { place, market, wilderness }

    @Getter @Setter private String id;
    @Getter @Setter private String visitedLocationId;
    @Getter @Setter private String name;
    @Getter @Setter private String description;
    @Getter @Setter private Type type = Type.place;
    @Getter @Setter private int mapx;
    @Getter @Setter private int mapy;

    private LocationTransitions transitions = new LocationTransitions();

    // TODO: why did I do this?
    transient private LocationItems locationItems = new LocationItems(this);
    transient private LocationNonPlayers nonPlayers = new LocationNonPlayers();

    public Location() {
    }

    public Location(String id) {
        this.id = id;
    }


    @JsonIgnore
    public LocationNonPlayers getLocationNonPlayers() {
        return nonPlayers;
    }

    @JsonIgnore
    public Collection<Item> getItems() {
        return locationItems.getItems();
    }

    public boolean hasItems() {
        return !locationItems.getItems().isEmpty();
    }

    public void addItem(Item item) {
        locationItems.addItem(item);
    }

    public Item removeItem(String itemName) {
        return locationItems.removeItem(itemName);
    }

    public void addTransition(Command command, LocationTransition transition) {
        this.transitions.addTransition(command, transition);
    }

    @JsonIgnore
    public LocationTransition getTransition(Command command) {
        return transitions.getTransition(command);
    }

    @JsonIgnore
    public List<LocationTransition> getTransitions() {
        return transitions.getTransitions();
    }

    @JsonIgnore
    public Set<Map.Entry<Command, LocationTransition>> getCommands() { return transitions.getTransitionCommands().entrySet(); }

    public String toString() {
        return "location:{id:"+id+",name:" + name + "}";
    }

    public void setMap(int x, int y) {
        this.mapx = x; this.mapy = y;
    }

    @Override
    public boolean equals(Object object) {
        try {
            return getId().equalsIgnoreCase(((Location)object).getId());
        } catch ( Exception e ) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    public Location clone() {
        Location location = new Location();
        location.setId(id);
        location.setName(name);
        location.setDescription(description);
        location.setType(type);
        location.setMap(getMapx(), getMapy());

        this.getItems().forEach(i -> {
                    Item item = i.clone();
                    location.addItem(item);
                });

        return location;
    }
}

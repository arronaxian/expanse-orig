package com.ds.expanse.app.api.controller.model;

import com.ds.expanse.app.api.command.Command;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Identifiable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Navigable locations on the map.
 */
public class Location implements Identifiable<String> {
    public enum Type { place, market, wilderness }

    @Getter @Setter private String id;
    @Getter @Setter private String name;
    @Getter @Setter private String description;
    @Getter @Setter private Type type = Type.place;
    @Getter @Setter private int mapx;
    @Getter @Setter private int mapy;

    /**
     * The location was altered by the user.
     */
    @JsonIgnore
    @Getter @Setter private boolean altered;

    private LocationTransitions transitions = new LocationTransitions();

    private LocationItems locationItems = new LocationItems(this);
    private LocationNonPlayers nonPlayers = new LocationNonPlayers();

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

    public boolean hasItem(String itemName) {
        return locationItems.getItems().stream().filter(item -> item.getName().equalsIgnoreCase(itemName)).count() > 0;
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
            Location location = (Location)object;
            boolean compare =   getId().equals(location.getId()) &&
                                getDescription().equals(location.getDescription()) &&
                                getName().equals(location.getName()) &&
                                getMapx() == location.getMapx() &&
                                getMapy() == location.getMapy() &&
                                getType().equals(location.getType());

            if ( compare ) {
                List<Item> intersection = locationItems.getItems()
                        .stream()
                        .filter(location.locationItems.getItems()::contains)
                        .collect(Collectors.toList());

                compare = intersection.size() == location.getItems().size();
            }

            return compare;
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
                    location.locationItems.addItem(item);
                });

        return location;
    }
}

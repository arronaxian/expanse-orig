package com.ds.expanse.app.api.loader;

import com.ds.expanse.app.api.controller.model.Item;
import com.ds.expanse.app.api.controller.model.Location;
import com.ds.expanse.app.api.controller.model.LocationTransition;
import com.ds.expanse.app.api.controller.model.Player;
import com.ds.expanse.app.api.loader.model.*;
import com.ds.expanse.app.command.TransitionCommand;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MapperTest {

    Mapper mapper = new Mapper();

    @Before
    public void testSetup() {
    }

    /**
     * Simple map from PlayerDO to Player validates top level properties with no child data.
     */
    @Test
    public void testMapToPlayer() {
        // Simple no recursion mapping test
        PlayerDO playerDo = createPlayerDO();

        Player player = mapper.toPlayer(playerDo);

        assertEquals("gene", player.getName());
        assertEquals("1", player.getId());
        assertEquals(100, player.getCoins());
        assertEquals(100, player.getHealth());
        assertTrue(player.getItemList().isEmpty());
        assertNull(player.getCurrentLocation());
    }

    /**
     * Simple map from PlayerDO to Player validates top level properties with no child data.
     */
    @Test
    public void testMapToPlayerWithCurrentLocation() {
        // Simple no recursion mapping test
        PlayerDO playerDO = createPlayerDO();

        LocationDO locationDO = createLocationDO("2");
        playerDO.setCurrentLocation(locationDO);

        Player player = mapper.toPlayer(playerDO);

        assertEquals("gene", player.getName());
        assertEquals("1", player.getId());
        assertEquals(100, player.getCoins());
        assertEquals(100, player.getHealth());
        assertEquals("2", player.getCurrentLocation().getId());
        assertTrue(player.getItemList().isEmpty());
    }

    /**
     * Simple map from PlayerDO to Player validates top level properties with no child data.
     */
    @Test
    public void testMapToPlayerDOWithCurrentLocation() {
        // Simple no recursion mapping test
        Player player = createPlayer();

        Location location = createLocation("2");
        player.setCurrentLocation(location);

        PlayerDO playerDO = mapper.toPlayerDO(player);

        assertEquals("gene", player.getName());
        assertEquals("1", player.getId());
        assertEquals(100, player.getCoins());
        assertEquals(100, player.getHealth());
        assertEquals("2", player.getCurrentLocation().getId());
        assertTrue(player.getItemList().isEmpty());
    }

    /**
     * Simple map from ItemDO to Item validates top level properties with no child data.
     */
    @Test
    public void testMapToItem() {
        ItemDO itemDO = createItemDO();

        Item item = mapper.toItem(itemDO);

        assertEquals("1", item.getId());
        assertEquals(1, item.getCost());
        assertEquals(2, item.getCount());
        assertEquals(10, item.getCriticalHitChance());
        assertEquals("item", item.getName());
        assertEquals(Item.Type.weapon, item.getType());
        assertEquals("item description", item.getDescription());
    }

    /**
     * Simple map from ItemDO to Item validates top level properties with no child data.
     */
    @Test
    public void testMapToItemDO() {
        Item item = createItem();

        ItemDO itemDO = mapper.toItemDO(item);

        assertEquals("1", itemDO.getId());
        assertEquals(1, itemDO.getCost());
        assertEquals(2, itemDO.getCount());
        assertEquals(10, itemDO.getCriticalHitChance());
        assertEquals("item", itemDO.getName());
        assertEquals(Item.Type.weapon.toString(), itemDO.getType());
        assertEquals("item description", itemDO.getDescription());
    }

    /**
     * Simple map from LocationDO to Location validates top level properties with no child data.
     */
    @Test
    public void testMapToLocation() {
        LocationDO locationDO = createLocationDO("1");

        Location location = mapper.toLocation(locationDO);

        assertEquals("1", location.getId());
        assertEquals("location1", location.getName());
        assertEquals(1, location.getMapx());
        assertEquals(2, location.getMapy());
        assertEquals("location description", location.getDescription());
        assertEquals(Location.Type.place, location.getType());
    }

    /**
     * Simple map from LocationDO to Location validates top level properties with no child data.
     */
    @Test
    public void testMapToLocationDO() {
        Location location = createLocation("1");

        LocationDO locationDO = mapper.toLocationDO(location);

        assertEquals("1", locationDO.getId());
        assertEquals("location1", locationDO.getName());
        assertEquals(1, locationDO.getMapx());
        assertEquals(2, locationDO.getMapy());
        assertEquals("location description 1", locationDO.getDescription());
        assertEquals(Location.Type.place.name(), locationDO.getType());
    }

    /**
     * Map from LocationTransitionDO to LocationTransition with Location child data.
     *
     * LocationTransition -> Location
     */
    @Test
    public void testMapToLocationTransition() {
        LocationTransitionDO locationTransitionDO = createLocationTransitionDO(createLocationDO("1"));

        LocationTransition locationTransition = mapper.toLocationTransition(locationTransitionDO);

        assertEquals("1", locationTransition.getId());
        assertEquals("location transition description", locationTransition.getDescription());
        assertEquals("north", locationTransition.getTransition());
        assertEquals("1", locationTransition.getLocation().getId());
    }

    /**
     * Map from PlayerDO to Player with Item child data.
     *
     * Player -> Item
     */
    @Test
    public void testMapToPlayerWithItem() {
        PlayerDO playerDO = createPlayerDO();
        playerDO.getItems().add(createItemDO());

        Player player = mapper.toPlayer(playerDO);
        assertEquals(1, player.getItemList().size());
    }

    /**
     * Map from LocationDO to Location with location transition child data.
     *
     * Location1 -> LocationTransition -> Location2
     */
    @Test
    public void testMapToLocationWithLocationTransition() {
        LocationDO locationDO1 = createLocationDO("1");
        LocationDO locationDO2 = createLocationDO("2");

        LocationTransitionDO locationTransitionDO = createLocationTransitionDO(locationDO2);
        locationDO1.getLocationTransitions().add(locationTransitionDO);

        Location location = mapper.toLocation(locationDO1);

        assertEquals(1, location.getTransitions().size());
        assertEquals("2", location.getTransitions().get(0).getLocation().getId());
    }

    /**
     * Map from Location to LocationDO with location transition child data reflexive.
     *
     * LocationDO1 -> LocationTransitionDO1 -> LocationDO2
     * LocationDO2 -> LocationTransitionDO2 -> LocationDO1
     */
    @Test
    public void testMapToLocationWithLocationTransitionReflexive() {
        LocationDO locationDO1 = createLocationDO("1");
        LocationDO locationDO2 = createLocationDO("2");

        LocationTransitionDO locationTransitionDO1to2 = createLocationTransitionDO("1", locationDO2);
        LocationTransitionDO locationTransitionDO2to1 = createLocationTransitionDO("2", locationDO1);

        locationDO1.getLocationTransitions().add(locationTransitionDO1to2);
        locationDO2.getLocationTransitions().add(locationTransitionDO2to1);

        Location location1 = mapper.toLocation(locationDO1);
        Location location2 = mapper.toLocation(locationDO2);

        assertEquals(1, location1.getTransitions().size());
        assertEquals("1", location1.getTransitions().get(0).getId());

        assertEquals(1, location2.getTransitions().size());
        assertEquals("2", location2.getTransitions().get(0).getId());
    }

    @Test
    public void testMapToLocationWithPlayerAlteredLocationDO() {
        PlayerDO player = createPlayerDO();
        LocationDO locationDO1 = createLocationDO("1");
        LocationDO locationDO2 = createLocationDO("2");

        PlayerAlteredLocationDO locationDOA = createPlayerAlteredLocationDO("1ABC", player, locationDO1);
        LocationTransitionDO locationTransitionDO1to2 = createLocationTransitionDO("1", locationDO2);
        LocationTransitionDO locationTransitionDO2to1 = createLocationTransitionDO("2", locationDO1);

        locationDO1.getLocationTransitions().add(locationTransitionDO1to2);
        locationDOA.getLocationTransitions().add(locationTransitionDO1to2);

        locationDO2.getLocationTransitions().add(locationTransitionDO2to1);

        Location location1 = mapper.toLocation(locationDOA);
        Location location2 = mapper.toLocation(locationDO2);

        assertEquals(1, location1.getTransitions().size());
        assertEquals("1", location1.getTransitions().get(0).getId());
        assertEquals("2", location1.getTransitions().get(0).getLocation().getId());

        assertEquals(1, location2.getTransitions().size());
        assertEquals("2", location2.getTransitions().get(0).getId());
        assertEquals("1", location2.getTransitions().get(0).getLocation().getId());
    }

    /**
     * Map from LocationDO to Location with location transition child data reflexive.
     *
     * Location1 -> LocationTransition -> Location2
     * Location2 -> LocationTransition -> Location1
     */
    @Test
    public void testMapToLocationDOWithLocationTransitionReflexive() {
        Location location1 = createLocation("1");
        Location location2 = createLocation("2");

        LocationTransition locationTransition1to2 = createLocationTransition("1", location2);
        LocationTransition locationTransition2to1 = createLocationTransition("2", location1);

        location1.addTransition(new TransitionCommand("move"), locationTransition1to2);
        location2.addTransition(new TransitionCommand("move"), locationTransition2to1);

        LocationDO locationDO1 = mapper.toLocationDO(location1);
        LocationDO locationDO2 = mapper.toLocationDO(location2);

        assertEquals(1, locationDO1.getLocationTransitions().size());
        assertEquals("1", locationDO1.getLocationTransitions().get(0).getId());

        assertEquals(1, locationDO2.getLocationTransitions().size());
        assertEquals("2", locationDO2.getLocationTransitions().get(0).getId());
    }

    private LocationTransitionDO createLocationTransitionDO(LocationDO toLocation) {
        return createLocationTransitionDO("1", toLocation);
    }

    private LocationTransitionDO createLocationTransitionDO(String id, LocationDO toLocation) {
        LocationTransitionDO locationTransitionDO = new LocationTransitionDO();
        locationTransitionDO.setId(id);
        locationTransitionDO.setDescription("location transition description");
        locationTransitionDO.setTransition("north");
        locationTransitionDO.setLocation(toLocation);

        return locationTransitionDO;
    }

    private PlayerAlteredLocationDO createPlayerAlteredLocationDO(String id, PlayerDO player, LocationDO originalLocation) {
        PlayerAlteredLocationDO locationDO = new PlayerAlteredLocationDO();

        locationDO.setId(id);
        locationDO.setName("location"+id);
        locationDO.setMapx(1);
        locationDO.setMapy(2);
        locationDO.setDescription("location description");
        locationDO.setType(Location.Type.place.name());
        locationDO.setLocation(originalLocation);
        locationDO.setPlayer(player);

        return locationDO;
    }

    private LocationTransition createLocationTransition(String id, Location toLocation) {
        LocationTransition locationTransition = new LocationTransition();
        locationTransition.setId(id);
        locationTransition.setDescription("location transition description " + id);
        locationTransition.setTransition("north " + id);
        locationTransition.setLocation(toLocation);

        return locationTransition;
    }

    private LocationDO createLocationDO(String id) {
        LocationDO locationDO = new LocationDO();
        locationDO.setId(id);
        locationDO.setName("location"+id);
        locationDO.setMapx(1);
        locationDO.setMapy(2);
        locationDO.setDescription("location description");
        locationDO.setType(Location.Type.place.name());
        return locationDO;
    }

    private Location createLocation(String id) {
        Location location = new Location();
        location.setId(id);
        location.setName("location"+id);
        location.setMapx(1);
        location.setMapy(2);
        location.setDescription("location description "+id);
        location.setType(Location.Type.place);

        return location;
    }

    private ItemDO createItemDO() {
        ItemDO itemDO = new ItemDO();
        itemDO.setId("1");
        itemDO.setCost(1);
        itemDO.setCount(2);
        itemDO.setCriticalHitChance(10);
        itemDO.setHitDamage(5);
        itemDO.setName("item");
        itemDO.setType("weapon");
        itemDO.setDetails("item details");
        itemDO.setDescription("item description");
        return itemDO;
    }

    private Item createItem() {
        Item item = new Item();
        item.setId("1");
        item.setCost(1);
        item.setCount(2);
        item.setCriticalHitChance(10);
        item.setHitDamage(5);
        item.setName("item");
        item.setType(Item.Type.weapon);
        item.setDescription("item description");
        return item;
    }

    private PlayerDO createPlayerDO() {
        PlayerDO playerDo = new PlayerDO();
        playerDo.setName("gene");
        playerDo.setEmailAddress("emailaddress");
        playerDo.setId("1");
        playerDo.setUid("uid");
        playerDo.setCoins(100);
        playerDo.setHealth(100);
        return playerDo;
    }

    private Player createPlayer() {
        Player player = new Player("gene");
        player.setId("1");
        player.setCoins(100);
        player.setHealth(100);
        return player;
    }

}

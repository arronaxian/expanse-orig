package com.ds.expanse.app.api.controller.model;

import java.util.ArrayList;
import java.util.List;

public class LocationNonPlayers {
    private List<NonPlayer> nonPlayers = new ArrayList<>();

    public void addNonPlayer(NonPlayer np) {
        nonPlayers.add(np);
    }

    public void removeNonPlayer(NonPlayer np) {
        nonPlayers.remove(np);
    }

    public List<NonPlayer> getNonPlayers() {
        return nonPlayers;
    }
}

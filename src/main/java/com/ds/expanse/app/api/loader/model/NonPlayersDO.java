package com.ds.expanse.app.api.loader.model;

import java.util.ArrayList;
import java.util.List;

public class NonPlayersDO {
    private List<NonPlayerDO> nonPlayers = new ArrayList<>();

    public List<NonPlayerDO> getNonPlayers() {
        return nonPlayers;
    }

    public void setNonPlayers(List<NonPlayerDO> nonPlayers) {
        this.nonPlayers = nonPlayers;
    }
}

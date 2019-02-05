package com.ds.expanse.app.api.loader;

import com.ds.expanse.app.api.controller.model.Item;
import com.ds.expanse.app.api.controller.model.Location;
import com.ds.expanse.app.api.controller.model.Market;
import com.ds.expanse.app.api.controller.model.NonPlayer;
import com.ds.expanse.app.api.loader.model.ItemDO;
import com.ds.expanse.app.nlp.ExpanseNLP;

import java.io.IOException;
import java.util.Map;

public interface Loader {
    /**
     * Gets the natural language processor.
     * @return A natural language processor instance.
     */
    ExpanseNLP getNLP();

    Market getMarket();

    void load() throws IOException;

    Map<String, NonPlayer> getNonPlayer();
}

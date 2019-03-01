package com.ds.expanse.app.service;

import com.ds.expanse.app.api.controller.model.MapElements;
import com.ds.expanse.app.api.loader.Loader;
import com.ds.expanse.app.api.loader.model.LocationDO;
import com.ds.expanse.app.api.loader.model.MapElementsDO;
import com.ds.expanse.app.api.service.MapElementsService;
import com.ds.expanse.app.repository.MapElementsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service("MapElementsPersistenceService")
public class MapElementsPersistenceService implements MapElementsService {
    @Autowired
    private MapElementsRepository mapElementsRepository;

    @Override
    public void saveMap(String name, InputStream jsonMapElementsStream, String startHereRoomName) {
        MapElementsDO mapElements = mapElementsRepository.findByName(name);
        if ( mapElements == null ) {
            mapElements = new MapElementsDO();
            mapElements.setName(name);
        }

        // load the json in.


    }
}

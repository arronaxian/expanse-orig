package com.ds.expanse.app.service;

import com.ds.expanse.app.api.controller.model.Location;
import com.ds.expanse.app.api.loader.Mapper;
import com.ds.expanse.app.api.loader.model.LocationDO;
import com.ds.expanse.app.repository.LocationRepository;
import com.ds.expanse.app.repository.LocationTransitionRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class LocationPersistenceService {
    @Autowired
    LocationRepository locationRepository;

    @Autowired
    LocationTransitionRepository locationTransitionRepository;

    public Location saveLocation(Location location) {
        Mapper mapper = new Mapper();

        final LocationDO locationDO;
        // New location.
        if (location.getId() == null ) {
            locationDO = locationRepository.insert(mapper.toLocationDO(location));
        } else {
            locationDO = locationRepository.save(mapper.toLocationDO(location));
        }

        return mapper.toLocation(locationDO);
    }
}

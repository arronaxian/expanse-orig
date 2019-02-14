package com.ds.expanse.app.repository;

import com.ds.expanse.app.api.loader.model.PlayerVisitedLocationDO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayerVisitedLocationRepository extends MongoRepository<PlayerVisitedLocationDO, String> {
    PlayerVisitedLocationDO findByPlayerIdAndLocationId(String playerId, String locationId);
}

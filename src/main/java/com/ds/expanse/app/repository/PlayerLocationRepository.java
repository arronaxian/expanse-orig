package com.ds.expanse.app.repository;

import com.ds.expanse.app.api.loader.model.PlayerLocationDO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayerLocationRepository extends MongoRepository<PlayerLocationDO, String> {
    PlayerLocationDO findByPlayerIdAAndLocationId(String playerId, String locationId);
}
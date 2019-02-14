package com.ds.expanse.app.repository;

import com.ds.expanse.app.api.loader.model.PlayerLocationDO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayerAlteredLocationRepository extends MongoRepository<PlayerLocationDO, String> {
    PlayerLocationDO findByPlayerIdAndLocationId(String playerId, String locationId);
}
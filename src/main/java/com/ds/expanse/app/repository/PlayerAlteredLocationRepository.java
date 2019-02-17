package com.ds.expanse.app.repository;

import com.ds.expanse.app.api.loader.model.PlayerAlteredLocationDO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayerAlteredLocationRepository extends MongoRepository<PlayerAlteredLocationDO, String> {
    PlayerAlteredLocationDO findByPlayerIdAndLocationId(String playerId, String locationId);
}
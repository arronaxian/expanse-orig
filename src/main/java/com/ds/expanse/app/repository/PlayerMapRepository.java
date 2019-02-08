package com.ds.expanse.app.repository;

import com.ds.expanse.app.api.loader.model.PlayerDO;
import com.ds.expanse.app.api.loader.model.PlayerMapDO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayerMapRepository extends MongoRepository<PlayerMapDO, String> {
}

package com.ds.expanse.app.repository;

import com.ds.expanse.app.api.loader.model.PlayerDO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayerRepository  extends MongoRepository<PlayerDO, String> {
    PlayerDO findByName(String name);
}

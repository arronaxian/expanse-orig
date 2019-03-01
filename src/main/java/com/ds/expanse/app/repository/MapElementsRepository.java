package com.ds.expanse.app.repository;

import com.ds.expanse.app.api.loader.model.MapElementsDO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MapElementsRepository extends MongoRepository<MapElementsDO, String> {
    MapElementsDO findByName(String name);
}

package com.ds.expanse.app.repository;

import com.ds.expanse.app.api.loader.model.LocationDO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LocationRepository  extends MongoRepository<LocationDO, String> {
    LocationDO findByName(String name);
}

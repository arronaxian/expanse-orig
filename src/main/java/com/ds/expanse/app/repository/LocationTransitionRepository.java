package com.ds.expanse.app.repository;

import com.ds.expanse.app.api.loader.model.LocationDO;
import com.ds.expanse.app.api.loader.model.LocationTransitionDO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LocationTransitionRepository extends MongoRepository<LocationTransitionDO, String> {
}

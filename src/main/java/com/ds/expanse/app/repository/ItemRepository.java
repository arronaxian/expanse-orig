package com.ds.expanse.app.repository;

import com.ds.expanse.app.api.loader.model.ItemDO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepository extends MongoRepository<ItemDO, String> {
    ItemDO findByName(String name);
}

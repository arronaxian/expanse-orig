package com.ds.expanse.app.service;

import com.ds.expanse.app.api.controller.model.Item;
import com.ds.expanse.app.api.loader.model.ItemDO;
import com.ds.expanse.app.api.service.ItemService;
import com.ds.expanse.app.api.loader.Mapper;
import com.ds.expanse.app.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ItemPersistenceService implements ItemService {
    @Autowired
    ItemRepository itemRepository;

    public Item save(Item item) {
        Mapper mapper = new Mapper();

        ItemDO itemDO = mapper.toItemDO(item);
        if ( item.isNew() ) {
            itemDO = itemRepository.insert(itemDO);
        } else {
            itemDO = itemRepository.save(itemDO);
        }

        return mapper.toItem(itemDO);
    }

    public Item findByName(String name) {
        Mapper mapper = new Mapper();

        return mapper.toItem(itemRepository.findByName(name));
    }
}

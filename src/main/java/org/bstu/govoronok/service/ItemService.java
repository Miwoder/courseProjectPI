package org.bstu.govoronok.service;

import org.bstu.govoronok.model.Item;
import org.bstu.govoronok.model.ItemType;
import org.bstu.govoronok.repository.ItemRepository;
import org.bstu.govoronok.repository.ItemTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    private ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> getAllItems(){
        return itemRepository.findAll();
    }

    public void saveItem(Item item){
        itemRepository.save(item);
    }
}

package org.bstu.govoronok.service;

import org.bstu.govoronok.model.Item;
import org.bstu.govoronok.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    private ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> getAllItems(){
        return itemRepository.findAll();
    }

    public Optional<Item> getItemById(Long id){
        return itemRepository.findById(id);
    }

    public void saveItem(Item item){
        itemRepository.save(item);
    }
}

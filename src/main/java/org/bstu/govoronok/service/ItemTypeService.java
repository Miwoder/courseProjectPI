package org.bstu.govoronok.service;

import org.bstu.govoronok.model.Auction;
import org.bstu.govoronok.model.ItemType;
import org.bstu.govoronok.repository.AuctionRepository;
import org.bstu.govoronok.repository.ItemTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemTypeService {
    private ItemTypeRepository itemTypeRepository;

    public ItemTypeService(ItemTypeRepository itemTypeRepository) {
        this.itemTypeRepository = itemTypeRepository;
    }

    public List<ItemType> getAllItemTypes(){
        return itemTypeRepository.findAll();
    }

    public ItemType getItemTypeByName(String name){
        return itemTypeRepository.getByName(name);
    }
}

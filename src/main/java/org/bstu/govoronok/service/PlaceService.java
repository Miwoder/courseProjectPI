package org.bstu.govoronok.service;

import org.bstu.govoronok.model.ItemType;
import org.bstu.govoronok.model.Place;
import org.bstu.govoronok.repository.ItemTypeRepository;
import org.bstu.govoronok.repository.PlaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceService {
    private PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public List<Place> getAllPlaces(){
        return placeRepository.findAll();
    }

    public Place getPlaceByName(String name){
        return placeRepository.getPlaceByName(name);
    }

}

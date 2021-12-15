package org.bstu.govoronok.repository;

import org.bstu.govoronok.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    Place getPlaceByName(String name);
}

package org.bstu.govoronok.repository;

import org.bstu.govoronok.model.Auction;
import org.bstu.govoronok.model.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemTypeRepository extends JpaRepository<ItemType, Long> {
    ItemType getByName(String name);
}

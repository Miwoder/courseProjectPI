package org.bstu.govoronok.repository;

import org.bstu.govoronok.model.AuctionStatus;
import org.bstu.govoronok.model.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionStatusRepository extends JpaRepository<AuctionStatus, Long> {
    AuctionStatus getAuctionStatusByName(String name);
}

package org.bstu.govoronok.repository;

import org.bstu.govoronok.model.Auction;
import org.bstu.govoronok.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> getAllByAuctionStatus_NameIsNotAndAuctionStatus_NameIsNot(String status, String status2);

    List<Auction> getAuctionsByAuctionStatus_Name(String name);

    List<Auction> getAuctionsByAuctionStatus_NameAndUser_Id(String name, Long id);

    List<Auction> getAuctionsByItem_User_Id(Long id);

    List<Auction> getAuctionsByItem_ItemType_NameAndAuctionStatus_NameIsNotAndAuctionStatus_NameIsNot(String type,
                         String status, String status2);

    List<Auction> getAuctionsByAuctionStatus_NameIsNotAndAuctionStatus_NameIsNot(String status, String status2);
}

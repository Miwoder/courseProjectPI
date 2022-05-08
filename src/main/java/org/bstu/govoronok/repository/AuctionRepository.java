package org.bstu.govoronok.repository;

import org.bstu.govoronok.model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {

    @Query(value = "select * from auction a where (a.statusid = 2 or a.statusid = 4) and a.itemid in " +
            "(select i.id from item i where i.name like %:keyword% or i.description like %:keyword%) ",
            nativeQuery = true)
    List<Auction> findByKeyword(@Param("keyword") String keyword);

    List<Auction> getAuctionsByItem_NameLike(String keyword);
    List<Auction> getAuctionsByItem_DescriptionLike(String keyword);

    List<Auction> getAllByAuctionStatus_NameIsNotAndAuctionStatus_NameIsNot(String status, String status2);

    List<Auction> getAuctionsByAuctionStatus_Name(String name);

    List<Auction> getAuctionsByAuctionStatus_NameAndUser_Id(String name, Long id);

    List<Auction> getAuctionsByItem_User_Id(Long id);

    List<Auction> getAuctionsByItem_ItemType_NameAndAuctionStatus_NameIsNotAndAuctionStatus_NameIsNot(String type,
                                                                                                      String status, String status2);

    List<Auction> getAuctionsByAuctionStatus_NameIsNotAndAuctionStatus_NameIsNot(String status, String status2);
}

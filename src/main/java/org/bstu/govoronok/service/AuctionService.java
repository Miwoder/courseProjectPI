package org.bstu.govoronok.service;

import org.bstu.govoronok.model.Auction;
import org.bstu.govoronok.repository.AuctionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuctionService {
    private AuctionRepository auctionRepository;

    public AuctionService(AuctionRepository auctionRepository) {
        this.auctionRepository = auctionRepository;
    }

    public List<Auction> getAllNotEndedAuctions(){
        return auctionRepository
                .getAllByAuctionStatus_NameIsNotAndAuctionStatus_NameIsNot("END", "Unconfirmed");
    }

    public void saveAuction(Auction auction){
        auctionRepository.save(auction);
    }

    public List<Auction> getAllUnconfirmedAuctions(){
        return auctionRepository.getAuctionsByAuctionStatus_Name("Unconfirmed");
    }

    public List<Auction> getAllWonAuctionsByUser(Long id){
        return auctionRepository.getAuctionsByAuctionStatus_NameAndUser_Id("END", id);
    }

    public List<Auction> getAllAuctionsByUser(Long id){
        return auctionRepository.getAuctionsByItem_User_Id(id);
    }

    public List<Auction> getAllConfirmedAuctions(){
        return auctionRepository.getAuctionsByAuctionStatus_NameIsNotAndAuctionStatus_NameIsNot("Unconfirmed",
                "END");
    }

    public void deleteAuctionById(Long id){
        auctionRepository.deleteById(id);
    }

    public Optional<Auction> getAuctionById(Long id){
        return auctionRepository.findById(id);
    }

    public List<Auction> getAuctionByType(String type){
        return auctionRepository.getAuctionsByItem_ItemType_NameAndAuctionStatus_NameIsNotAndAuctionStatus_NameIsNot(type,
                "END", "Unconfirmed");
    }
}

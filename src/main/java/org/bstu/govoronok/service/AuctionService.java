package org.bstu.govoronok.service;

import lombok.RequiredArgsConstructor;
import org.bstu.govoronok.model.Auction;
import org.bstu.govoronok.model.StatusHistory;
import org.bstu.govoronok.repository.AuctionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuctionService {

    Logger logger = LoggerFactory.getLogger(AuctionService.class);

    private final AuctionRepository auctionRepository;
    private final AuctionStatusService auctionStatusService;
    private final StatusHistoryService statusHistoryService;

    public List<Auction> getAllNotEndedAuctions() {
        return auctionRepository
                .getAllByAuctionStatus_NameIsNotAndAuctionStatus_NameIsNot("END", "Unconfirmed");
    }

    public void saveAuction(Auction auction) {
        auctionRepository.save(auction);
    }

    public List<Auction> getAllUnconfirmedAuctions() {
        return auctionRepository.getAuctionsByAuctionStatus_Name("Unconfirmed");
    }

    public List<Auction> getAllWonAuctionsByUser(Long id) {
        return auctionRepository.getAuctionsByAuctionStatus_NameAndUser_Id("END", id);
    }

    public List<Auction> getAllAuctionsByUser(Long id) {
        return auctionRepository.getAuctionsByItem_User_Id(id);
    }

    public List<Auction> getAllConfirmedAuctions() {
        return auctionRepository.getAuctionsByAuctionStatus_NameIsNotAndAuctionStatus_NameIsNot("Unconfirmed",
                "END");
    }

    public void deleteAuctionById(Long id) {
        auctionRepository.deleteById(id);
    }

    public Optional<Auction> getAuctionById(Long id) {
        return auctionRepository.findById(id);
    }

    public List<Auction> getAuctionByType(String type) {
        return auctionRepository.getAuctionsByItem_ItemType_NameAndAuctionStatus_NameIsNotAndAuctionStatus_NameIsNot(type,
                "END", "Unconfirmed");
    }

    public void updateAuctionsStatuses() {
        List<Auction> auctions = getAllConfirmedAuctions();
        for (Auction auction : auctions) {
            if (auction.getStartDate().isAfter(LocalDate.now())) {
                auction.setAuctionStatus(auctionStatusService.getAuctionStatusByName("Starts soon"));
            } else {
                auction.setAuctionStatus(auctionStatusService.getAuctionStatusByName("Ongoing"));
            }
            saveAuction(auction);
            StatusHistory statusHistory = new StatusHistory(LocalDate.now(), auction.getAuctionStatus(), auction);
            statusHistoryService.save(statusHistory);
        }
        logger.info("Auction statuses updated");
    }

    public void updateAuctionStatus(Long auctionId) {
        Auction auction = getAuctionById(auctionId).get();
        if (auction.getEndDate().isBefore(LocalDate.now())) {
            auction.setAuctionStatus(auctionStatusService.getAuctionStatusByName("END"));
        } else if (auction.getStartDate().isAfter(LocalDate.now())) {
            auction.setAuctionStatus(auctionStatusService.getAuctionStatusByName("Starts soon"));
        } else {
            auction.setAuctionStatus(auctionStatusService.getAuctionStatusByName("Ongoing"));
        }
        saveAuction(auction);
        StatusHistory statusHistory = new StatusHistory(LocalDate.now(), auction.getAuctionStatus(), auction);
        statusHistoryService.save(statusHistory);
        logger.info("Auction statuses updated");
    }
}

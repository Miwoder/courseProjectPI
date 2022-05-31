package org.bstu.govoronok.service;

import lombok.RequiredArgsConstructor;
import org.bstu.govoronok.model.Auction;
import org.bstu.govoronok.model.BetHistory;
import org.bstu.govoronok.model.StatusHistory;
import org.bstu.govoronok.model.User;
import org.bstu.govoronok.repository.AuctionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuctionService {

    Logger logger = LoggerFactory.getLogger(AuctionService.class);

    private final AuctionRepository auctionRepository;
    private final AuctionStatusService auctionStatusService;
    private final StatusHistoryService statusHistoryService;
    private final BetHistoryService betHistoryService;
    private final UserService userService;

    public List<Auction> getAllNotEndedAuctions() {
        List<Auction> auctions = auctionRepository
                .getAllByAuctionStatus_NameIsNotAndAuctionStatus_NameIsNot("END", "Unconfirmed");
        auctions.sort(Comparator.comparing(Auction::getEndDate));
        return auctions;
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

    @Transactional
    public void updateAuctionsStatuses() {
        List<Auction> auctions = getAllConfirmedAuctions();
        for (Auction auction : auctions) {
            if (!auction.getAuctionStatus().getName().equals("END") &&
                    !auction.getAuctionStatus().getName().equals("Unconfirmed")) {
                if (auction.getStartDate().isBefore(LocalDate.now()) && auction.getEndDate().isAfter(LocalDate.now())) {
                    auction.setAuctionStatus(auctionStatusService.getAuctionStatusByName("Ongoing"));
                } else if (auction.getStartDate().isAfter(LocalDate.now())) {
                    auction.setAuctionStatus(auctionStatusService.getAuctionStatusByName("Starts soon"));
                } else if (auction.getEndDate().isBefore(LocalDate.now())) {
                    auction.setAuctionStatus(auctionStatusService.getAuctionStatusByName("END"));
                }
                saveAuction(auction);
                StatusHistory statusHistory = new StatusHistory(LocalDate.now(), auction.getAuctionStatus(), auction);
                statusHistoryService.save(statusHistory);
            }
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

    @Transactional
    public List<Auction> getAuctionsByKey(String key) {
        List<Auction> auctions = new ArrayList<>();
        auctions.addAll(auctionRepository.findByKeyword(key));
        return auctions;
    }

    @Transactional
    public void makeBet(Long auctionId, Principal principal, String bet) {
        Optional<Auction> auction = getAuctionById(auctionId);
        Optional<User> currentUser = Optional.ofNullable(userService.findByUsername(principal.getName()));
        if (auction.isPresent() && currentUser.isPresent()) {
            BigDecimal highBet = new BigDecimal(auction.get().getHighBet());
            if (null != bet && !bet.isEmpty() && Integer.parseInt(auction.get().getHighBet()) < Integer.parseInt(bet)
                    && currentUser.get().getBalance().compareTo(highBet) == 1
                    && auction.get().getAuctionStatus().getName()
                    .equals(auctionStatusService.getAuctionStatusByName("Ongoing").getName())) {
                User lastWinner = userService.getUserById(auction.get().getUser().getId());
                BigDecimal lastHighBet = new BigDecimal(auction.get().getHighBet());
                lastWinner.setBalance(lastWinner.getBalance().add(lastHighBet));
                userService.updateUser(lastWinner);

                BigDecimal newHighBet = new BigDecimal(bet);
                currentUser.get().setBalance(currentUser.get().getBalance().subtract(newHighBet));
                userService.updateUser(currentUser.get());

                auction.get().setHighBet(bet);
                auction.get().setUser(currentUser.get());
                saveAuction(auction.get());
                BetHistory betHistory = new BetHistory(bet, LocalDate.now(), currentUser.get(), auction.get());
                betHistoryService.save(betHistory);
            }
        }
    }
}

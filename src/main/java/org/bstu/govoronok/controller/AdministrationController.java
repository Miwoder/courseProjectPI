package org.bstu.govoronok.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bstu.govoronok.model.Auction;
import org.bstu.govoronok.model.BetHistory;
import org.bstu.govoronok.model.StatusHistory;
import org.bstu.govoronok.model.User;
import org.bstu.govoronok.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@Slf4j
public class AdministrationController {

    private final UserService userService;
    private final AuctionService auctionService;
    private final ItemService itemService;
    private final PlaceService placeService;
    private final AuctionStatusService auctionStatusService;
    private final BetHistoryService betHistoryService;
    private final StatusHistoryService statusHistoryService;

    @GetMapping("/administration")
    public String getAdminPage(Model model, Principal principal) {
        model.addAttribute("auctions", auctionService.getAllUnconfirmedAuctions());
        return "/auction/unconfirmedAuctions";
    }

    @DeleteMapping("/administration/{id}")
    public String deleteAuction(@PathVariable("id") Long auctionId) {
        auctionService.deleteAuctionById(auctionId);
        return "redirect:/administration";
    }

    @PatchMapping("/administration/{id}")
    public String approveAuction(@PathVariable("id") Long auctionId) {
        Auction auction = auctionService.getAuctionById(auctionId).get();
        if(auction.getEndDate().isBefore(LocalDate.now())){
            auction.setAuctionStatus(auctionStatusService.getAuctionStatusByName("END"));
        }
        else if(auction.getStartDate().isAfter(LocalDate.now())){
            auction.setAuctionStatus(auctionStatusService.getAuctionStatusByName("Starts soon"));
        }
        else{
            auction.setAuctionStatus(auctionStatusService.getAuctionStatusByName("Ongoing"));
        }
        auctionService.saveAuction(auction);
        StatusHistory statusHistory = new StatusHistory(LocalDate.now(), auction.getAuctionStatus(), auction);
        statusHistoryService.save(statusHistory);
        return "redirect:/administration";
    }
}
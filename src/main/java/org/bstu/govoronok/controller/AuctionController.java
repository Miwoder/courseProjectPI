package org.bstu.govoronok.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bstu.govoronok.model.*;
import org.bstu.govoronok.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
@Slf4j
public class AuctionController {

    private final UserService userService;
    private final AuctionService auctionService;
    private final PaymentService paymentService;
    private final ItemTypeService itemTypeService;
    private final PlaceService placeService;
    private final AuctionStatusService auctionStatusService;

    @GetMapping("/auctions/all")
    public String getAllAuctions(Model model) {
        model.addAttribute("auctions", auctionService.getAllNotEndedAuctions());
        return "/auction/auctions";
    }

    @GetMapping("/auctions/add/auction")
    public String getFormForNewAuction(Model model) {
        model.addAttribute("auction", new Auction());
        return "/auction/addNewAuction";
    }

    @PostMapping("/auctions/add/auction")
    public String addNewAuction(Model model, @ModelAttribute("auction") Auction auction, Principal principal) {
        auction.setItem((Item) model.getAttribute("item"));
        auction.setPlace(placeService.getPlaceByName("NY Auctions"));
        auction.setHighBet(auction.getStartBet());
        auction.setUser(userService.findByUsername(principal.getName()));
        auction.setAuctionStatus(auctionStatusService.getAuctionStatusByName("Unconfirmed"));
        auctionService.saveAuction(auction);
        return "redirect:/auctions/all";
    }

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
        return "redirect:/administration";
    }

    @GetMapping("/my/won")
    public String getWonAuctions(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("auctions", auctionService.getAllWonAuctionsByUser(user.getId()));
        return "/auction/wonAuctions";
    }

    @GetMapping("/auctions/{id}")
    public String getWonAuctions(Model model, @PathVariable("id") Long auctionId, Principal principal) {
        model.addAttribute("auction", auctionService.getAuctionById(auctionId));
        return "/auction/auction";
    }

    @GetMapping("/my/auctions")
    public String getMyAuctions(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("auctions", auctionService.getAllAuctionsByUser(user.getId()));
        return "/auction/auctions";
    }

}
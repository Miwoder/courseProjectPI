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
import java.util.Optional;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
@Slf4j
public class AuctionController {

    private final UserService userService;
    private final AuctionService auctionService;
    private final ItemService itemService;
    private final PlaceService placeService;
    private final AuctionStatusService auctionStatusService;
    private final BetHistoryService betHistoryService;
    private final StatusHistoryService statusHistoryService;

    @GetMapping("/auctions/all")
    public String getAllAuctions(Model model) {
        model.addAttribute("auctions", auctionService.getAllNotEndedAuctions());
        return "/auction/auctions";
    }

    @GetMapping("/items/{itemId}/auction/add")
    public String getFormForNewAuction(Model model, @PathVariable("itemId") Long itemId) {
        model.addAttribute("itemId", itemId);
        model.addAttribute("auction", new Auction());
        model.addAttribute("places", placeService.getAllPlaces());
        return "/auction/addNewAuction";
    }

    @PostMapping("/items/{itemId}/auction/add")
    public String addNewAuction(@PathVariable("itemId") Long itemId,
                                @RequestParam("placeName") String placeName,
                                @ModelAttribute("auction") Auction auction, Principal principal) {
        auction.setItem(itemService.getItemById(itemId).get());
        auction.setPlace(placeService.getPlaceByName(placeName));
        auction.setHighBet(auction.getStartBet());
        auction.setUser(userService.findByUsername(principal.getName()));
        auction.setAuctionStatus(auctionStatusService.getAuctionStatusByName("Unconfirmed"));
        auctionService.saveAuction(auction);
        return "redirect:/auctions/all";
    }

    @GetMapping("/auctions/{id}")
    public String getWonAuctions(Model model, @PathVariable("id") Long auctionId) {
        model.addAttribute("auction", auctionService.getAuctionById(auctionId));

        return "/auction/auction";
    }

    @PatchMapping("/auctions/{id}")
    public String makeBet(@PathVariable("id") Long auctionId, Principal principal, String bet) {
        Optional<Auction> auction = auctionService.getAuctionById(auctionId);
        if(Integer.parseInt(auction.get().getHighBet()) < Integer.parseInt(bet) &&
                auction.get().getAuctionStatus().getName().equals(auctionStatusService.getAuctionStatusByName("Ongoing").getName())){
            auction.get().setHighBet(bet);
            User user = userService.findByUsername(principal.getName());
            auction.get().setUser(user);
            auctionService.saveAuction(auction.get());
            BetHistory betHistory = new BetHistory(bet, LocalDate.now(), user, auction.get());
            betHistoryService.save(betHistory);
        }
        return "redirect:/auctions/{id}";
    }

    @GetMapping("/auctions/cars")
    public String getAuctionsWithCars(Model model) {
        model.addAttribute("auctions", auctionService.getAuctionByType("Cars"));
        return "/auction/auctions";
    }

    @GetMapping("/auctions/furniture")
    public String getAuctionsWithFurniture(Model model) {
        model.addAttribute("auctions", auctionService.getAuctionByType("Furniture"));
        return "/auction/auctions";
    }

    @GetMapping("/auctions/houses")
    public String getAuctionsWithHouses(Model model) {
        model.addAttribute("auctions", auctionService.getAuctionByType("Houses"));
        return "/auction/auctions";
    }

    @GetMapping("/auctions/animals")
    public String getAuctionsWithAnimals(Model model) {
        model.addAttribute("auctions", auctionService.getAuctionByType("Animals"));
        return "/auction/auctions";
    }

    @GetMapping("/auctions/materials")
    public String getAuctionsWithMaterials(Model model) {
        model.addAttribute("auctions", auctionService.getAuctionByType("Materials"));
        return "/auction/auctions";
    }
}
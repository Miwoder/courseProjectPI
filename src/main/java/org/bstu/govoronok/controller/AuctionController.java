package org.bstu.govoronok.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bstu.govoronok.model.Auction;
import org.bstu.govoronok.model.BetHistory;
import org.bstu.govoronok.model.User;
import org.bstu.govoronok.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;


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

    @GetMapping("/auctions/all")
    public String getAllAuctions(Model model) {
        model.addAttribute("auctions", auctionService.getAllNotEndedAuctions());
        return "/auction/auctions";
    }

    @GetMapping("/items/{itemId}/auction/add")
    public String getFormForNewAuction(Model model, @PathVariable("itemId") Long itemId) {
        model.addAttribute("auction", new Auction());
        model.addAttribute("places", placeService.getAllPlaces());
        return "/auction/addNewAuction";
    }

    @PostMapping("/items/{itemId}/auction/add")
    public String addNewAuction(@PathVariable("itemId") Long itemId, Model model,
                                @RequestParam("placeName") String placeName,
                                @Valid Auction auction, Errors errors, Principal principal) {
        if (errors.hasErrors()) {
            model.addAttribute("places", placeService.getAllPlaces());
            return "/auction/addNewAuction";
        }
        auction.setItem(itemService.getItemById(itemId).get());
        auction.setPlace(placeService.getPlaceByName(placeName));
        auction.setHighBet(auction.getStartBet());
        auction.setUser(userService.findByUsername("user"));
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
        auctionService.makeBet(auctionId, principal, bet);
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

    @GetMapping("/auctions/ssd")
    public String getAuctionsWithSSD(Model model) {
        model.addAttribute("auctions", auctionService.getAuctionByType("SSD"));
        return "/auction/auctions";
    }

    @GetMapping("/auctions/power_supplies")
    public String getAuctionsWithPowerSupplies(Model model) {
        model.addAttribute("auctions", auctionService.getAuctionByType("Power supplie"));
        return "/auction/auctions";
    }

    @GetMapping("/auctions/video_cards")
    public String getAuctionsWithVideoCards(Model model) {
        model.addAttribute("auctions", auctionService.getAuctionByType("Video card"));
        return "/auction/auctions";
    }

    @GetMapping("/auctions/hard_disks")
    public String getAuctionsWithHardDisks(Model model) {
        model.addAttribute("auctions", auctionService.getAuctionByType("Hard disk"));
        return "/auction/auctions";
    }

    @GetMapping("/auctions/sound_cards")
    public String getAuctionsWithSoundCards(Model model) {
        model.addAttribute("auctions", auctionService.getAuctionByType("Sound card"));
        return "/auction/auctions";
    }

    @GetMapping("/auctions/corps")
    public String getAuctionsWithCorps(Model model) {
        model.addAttribute("auctions", auctionService.getAuctionByType("Corp"));
        return "/auction/auctions";
    }

    @GetMapping("/auctions/motherboards")
    public String getAuctionsWithMotherboards(Model model) {
        model.addAttribute("auctions", auctionService.getAuctionByType("Motherboard"));
        return "/auction/auctions";
    }

    @GetMapping("/auctions/ram")
    public String getAuctionsWithRam(Model model) {
        model.addAttribute("auctions", auctionService.getAuctionByType("RAM"));
        return "/auction/auctions";
    }

    @GetMapping("/auctions/optical_drive")
    public String getAuctionsWithOpticalDrive(Model model) {
        model.addAttribute("auctions", auctionService.getAuctionByType("Optical drive"));
        return "/auction/auctions";
    }

    @GetMapping("/auctions/processors")
    public String getAuctionsWithProcessors(Model model) {
        model.addAttribute("auctions", auctionService.getAuctionByType("Processor"));
        return "/auction/auctions";
    }
}
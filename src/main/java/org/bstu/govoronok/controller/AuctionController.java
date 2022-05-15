package org.bstu.govoronok.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bstu.govoronok.model.Auction;
import org.bstu.govoronok.provider.SimpleDataProvider;
import org.bstu.govoronok.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;


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
        model.addAttribute("simpleDataProvider", new SimpleDataProvider());
        return "/auction/auctions";
    }

    @GetMapping("/auctions/find")
    public String findAuctions(@ModelAttribute("simpleDataProvider") SimpleDataProvider simpleDataProvider,
                               Model model) {
        List<Auction> auctions = auctionService.getAuctionsByKey(simpleDataProvider.getTextField());
        model.addAttribute("auctions", auctions);
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
        auction.setUser(userService.findByUsername(principal.getName()));
        auction.setAuctionStatus(auctionStatusService.getAuctionStatusByName("Unconfirmed"));
        auctionService.saveAuction(auction);
        return "redirect:/auctions/all";
    }

    @GetMapping("/auctions/auction/{id}")
    public String getAuction(Model model, @PathVariable("id") Long auctionId) {
        Auction auction = auctionService.getAuctionById(auctionId).get();
        model.addAttribute("auction", auctionService.getAuctionById(auctionId));

        LocalDate now = LocalDate.now();
        Calendar calendar = Calendar.getInstance();

        String expirationTime =(auction.getEndDate().getDayOfYear() - now.getDayOfYear()) + "days " +
                (24 - calendar.get(Calendar.HOUR_OF_DAY)) + "hours";

        model.addAttribute("expiredTime", expirationTime);
        return "/auction/auction";
    }

    @PatchMapping("/auctions/auction/{id}")
    public String makeBet(@PathVariable("id") Long auctionId, Principal principal, String bet) {
        auctionService.makeBet(auctionId, principal, bet);
        return "redirect:/auctions/auction/{id}";
    }

    @GetMapping("/auctions/ssd")
    public String getAuctionsWithSSD(Model model) {
        model.addAttribute("auctions", auctionService.getAuctionByType("SSD"));
        model.addAttribute("simpleDataProvider", new SimpleDataProvider());
        return "/auction/auctions";
    }

    @GetMapping("/auctions/power_supplies")
    public String getAuctionsWithPowerSupplies(Model model) {
        model.addAttribute("auctions", auctionService.getAuctionByType("Power supplie"));
        model.addAttribute("simpleDataProvider", new SimpleDataProvider());
        return "/auction/auctions";
    }

    @GetMapping("/auctions/video_cards")
    public String getAuctionsWithVideoCards(Model model) {
        model.addAttribute("auctions", auctionService.getAuctionByType("Video card"));
        model.addAttribute("simpleDataProvider", new SimpleDataProvider());
        return "/auction/auctions";
    }

    @GetMapping("/auctions/hard_disks")
    public String getAuctionsWithHardDisks(Model model) {
        model.addAttribute("auctions", auctionService.getAuctionByType("Hard disk"));
        model.addAttribute("simpleDataProvider", new SimpleDataProvider());
        return "/auction/auctions";
    }

    @GetMapping("/auctions/sound_cards")
    public String getAuctionsWithSoundCards(Model model) {
        model.addAttribute("auctions", auctionService.getAuctionByType("Sound card"));
        model.addAttribute("simpleDataProvider", new SimpleDataProvider());
        return "/auction/auctions";
    }

    @GetMapping("/auctions/corps")
    public String getAuctionsWithCorps(Model model) {
        model.addAttribute("auctions", auctionService.getAuctionByType("Corp"));
        model.addAttribute("simpleDataProvider", new SimpleDataProvider());
        return "/auction/auctions";
    }

    @GetMapping("/auctions/motherboards")
    public String getAuctionsWithMotherboards(Model model) {
        model.addAttribute("auctions", auctionService.getAuctionByType("Motherboard"));
        model.addAttribute("simpleDataProvider", new SimpleDataProvider());
        return "/auction/auctions";
    }

    @GetMapping("/auctions/ram")
    public String getAuctionsWithRam(Model model) {
        model.addAttribute("auctions", auctionService.getAuctionByType("RAM"));
        model.addAttribute("simpleDataProvider", new SimpleDataProvider());
        return "/auction/auctions";
    }

    @GetMapping("/auctions/optical_drive")
    public String getAuctionsWithOpticalDrive(Model model) {
        model.addAttribute("auctions", auctionService.getAuctionByType("Optical drive"));
        model.addAttribute("simpleDataProvider", new SimpleDataProvider());
        return "/auction/auctions";
    }

    @GetMapping("/auctions/processors")
    public String getAuctionsWithProcessors(Model model) {
        model.addAttribute("auctions", auctionService.getAuctionByType("Processor"));
        model.addAttribute("simpleDataProvider", new SimpleDataProvider());
        return "/auction/auctions";
    }
}
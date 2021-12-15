package org.bstu.govoronok.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bstu.govoronok.model.Auction;
import org.bstu.govoronok.model.StatusHistory;
import org.bstu.govoronok.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final UserService userService;
    private final AuctionService auctionService;
    private final ItemService itemService;
    private final PlaceService placeService;
    private final AuctionStatusService auctionStatusService;
    private final BetHistoryService betHistoryService;
    private final StatusHistoryService statusHistoryService;

    @GetMapping("/")
    public String redirectToHome(){
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String getHomePage(){
        return "home/home";
    }

}
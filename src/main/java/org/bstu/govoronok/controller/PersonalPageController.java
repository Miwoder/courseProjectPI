package org.bstu.govoronok.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bstu.govoronok.model.Role;
import org.bstu.govoronok.model.User;
import org.bstu.govoronok.service.AuctionService;
import org.bstu.govoronok.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;


@Controller
@RequiredArgsConstructor
@Slf4j
public class PersonalPageController {

    private final UserService userService;
    private final AuctionService auctionService;

    @GetMapping("/my/won")
    public String getWonAuctions(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("auctions", auctionService.getAllWonAuctionsByUser(user.getId()));
        return "/auction/wonAuctions";
    }

    @GetMapping("/my/auctions")
    public String getMyAuctions(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("auctions", auctionService.getAllAuctionsByUser(user.getId()));
        return "/auction/auctions";
    }

    @GetMapping("/my")
    public String getMyPage(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        if (user.getRole().equals(Role.USER)) {
            return "user/myPage";
        } else {
            return "user/myPageAdmin";
        }
    }}
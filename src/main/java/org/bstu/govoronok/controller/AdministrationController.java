package org.bstu.govoronok.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.bstu.govoronok.model.Auction;
import org.bstu.govoronok.model.StatusHistory;
import org.bstu.govoronok.service.AuctionService;
import org.bstu.govoronok.service.AuctionStatusService;
import org.bstu.govoronok.service.StatusHistoryService;
import org.bstu.govoronok.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@Slf4j
public class AdministrationController {

    private final AuctionService auctionService;
    private final UserService userService;

    @GetMapping("/administration")
    public String getAdminPage(Model model) {
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
        Optional<Auction> auction = auctionService.getAuctionById(auctionId);
        auction.get().setUser(userService.findByUsername("user@gmail.com"));
        auctionService.saveAuction(auction.get());
        auctionService.updateAuctionStatus(auctionId);
        return "redirect:/administration";
    }

    @PatchMapping("/administration/updateStatuses")
    public String updateAuctionStatuses() {
        auctionService.updateAuctionsStatuses();
        return "redirect:/my";
    }
}
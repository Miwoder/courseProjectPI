package org.bstu.govoronok.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bstu.govoronok.model.Auction;
import org.bstu.govoronok.model.StatusHistory;
import org.bstu.govoronok.service.AuctionService;
import org.bstu.govoronok.service.AuctionStatusService;
import org.bstu.govoronok.service.StatusHistoryService;
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
public class AdministrationController {

    private final AuctionService auctionService;

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
        auctionService.updateAuctionStatus(auctionId);
        return "redirect:/administration";
    }

    @PatchMapping("/administration/updateStatuses")
    public String updateAuctionStatuses() {
        auctionService.updateAuctionsStatuses();
        return "redirect:/my";
    }
}
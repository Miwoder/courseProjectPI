package org.bstu.govoronok.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bstu.govoronok.model.Auction;
import org.bstu.govoronok.model.Role;
import org.bstu.govoronok.model.StatusHistory;
import org.bstu.govoronok.model.User;
import org.bstu.govoronok.service.AuctionService;
import org.bstu.govoronok.service.PaymentService;
import org.bstu.govoronok.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.time.LocalDate;


@Controller
@RequiredArgsConstructor
@Slf4j
public class PersonalPageController {

    private final UserService userService;
    private final AuctionService auctionService;
    private final PaymentService paymentService;

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
    }

    @PatchMapping("/my")
    public String approveAuction(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        if(user.getPayment().getId()==1){
            user.setPayment(paymentService.getPaymentById(2L).get());
            }
        else {
            user.setPayment(paymentService.getPaymentById(1L).get());
        }
        userService.updateUser(user);
        return "redirect:/my";
    }
}
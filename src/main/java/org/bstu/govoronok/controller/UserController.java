package org.bstu.govoronok.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bstu.govoronok.model.*;
import org.bstu.govoronok.service.AuctionService;
import org.bstu.govoronok.service.AuctionStatusService;
import org.bstu.govoronok.service.PaymentService;
import org.bstu.govoronok.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final PaymentService paymentService;
    private final AuctionService auctionService;
    private final AuctionStatusService auctionStatusService;

    @GetMapping("/")
    public String redirectToHome(){
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String getHomePage(){
        return "home/home";
    }

    @GetMapping("/signup")
    public String createNewUser(Model model) {
        model.addAttribute("user", new User());
        return "authentication/signup";
    }

    @PostMapping("/signup")
    public String addNewUser(@ModelAttribute("user") User user) {
        user.setPayment(paymentService.getPaymentByName("Cash"));
        userService.addNewUser(user);
        return "redirect:/authentication/confirmAlert";
    }

    @GetMapping("/signin")
    public String signin(){
        return "/authentication/signin";
    }

    @GetMapping("/authentication/forgot_password")
    public String resetPassword(){
        return "/authentication/resetPassword";
    }

    @PostMapping("/authentication/forgot_password")
    public String resetPassword(Model model, @ModelAttribute("username") String username) {
        userService.resetPasswordForUserByEmail(username);
        return "redirect:/authentication/confirmAlert";
    }

    @GetMapping("/authentication/reset")
    public String setNewPassword(){
        return "authentication/newPassword";
    }

    @PostMapping("/authentication/reset")
    public String setNewPassword(@ModelAttribute("code") UUID code,
                                 String password, String passwordConfirm) {
        if(password.equals(passwordConfirm)){
            userService.setNewPasswordWithCode(code , password);
        }
        return "redirect:/signin";
    }

    @GetMapping("/authentication/confirm/{code}")
    public String confirmUserCode(@PathVariable("code") UUID code) {
        userService.confirmUser(code);
        return "redirect:/signin";
    }

    @GetMapping("/authentication/confirmAlert")
    public String getConfirmAlert() {
        return "/authentication/confirmAlert";
    }

    @GetMapping("/my")
    public String getMyPage(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute( "user", user);
        if(user.getRole().equals(Role.USER)){
            return "user/myPage";
        }
        else{
            return "user/myPageAdmin";
        }
    }

    @PatchMapping("/administration/updateStatuses")
    public String updateAuctionStatuses() {
        List<Auction> auctions = auctionService.getAllConfirmedAuctions();
        for (Auction auction : auctions){
            if(auction.getStartDate().isAfter(LocalDate.now())){
                auction.setAuctionStatus(auctionStatusService.getAuctionStatusByName("Starts soon"));
            }
            else{
                auction.setAuctionStatus(auctionStatusService.getAuctionStatusByName("Ongoing"));
            }
            auctionService.saveAuction(auction);
        }
        return "redirect:/my";
    }
}
package org.bstu.govoronok.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bstu.govoronok.model.CreditCard;
import org.bstu.govoronok.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final UserService userService;

    @GetMapping("/deposit")
    public String deposit(Model model) {
        model.addAttribute("card", new CreditCard());
        return "payment/deposit";
    }

    @PostMapping("/deposit")
    public String newDeposit(@Valid @ModelAttribute("card") CreditCard creditCard, Errors errors, Principal principal) {
        if (errors.hasErrors()) {
            return "payment/deposit";
        } else {
            userService.deposit(creditCard.getAmount(), principal);
            log.info("Payment complete");
        }
        return "redirect:/my";
    }
}

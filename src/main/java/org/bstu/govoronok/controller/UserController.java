package org.bstu.govoronok.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bstu.govoronok.model.User;
import org.bstu.govoronok.service.PaymentService;
import org.bstu.govoronok.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final PaymentService paymentService;

    @GetMapping("/signup")
    public String createNewUser(Model model) {
        model.addAttribute("user", new User());
        return "authentication/signup";
    }

    @PostMapping("/signup")
    public String addNewUser(@Valid @ModelAttribute("user") User user, Errors errors) {
        if (errors.hasErrors()) {
            return "authentication/signup";
        }
        user.setPayment(paymentService.getPaymentByName("Cash"));
        userService.addNewUser(user);
        return "redirect:/authentication/confirmAlert";
    }

    @GetMapping("/signin")
    public String signin() {
        return "/authentication/signin";
    }

    @GetMapping("/authentication/forgot_password")
    public String resetPassword() {
        return "/authentication/resetPassword";
    }

    @PostMapping("/authentication/forgot_password")
    public String resetPassword(@ModelAttribute("username") String username) {
        userService.resetPasswordForUserByEmail(username);
        return "redirect:/authentication/confirmAlert";
    }

    @GetMapping("/authentication/reset")
    public String setNewPassword() {
        return "authentication/newPassword";
    }

    @PostMapping("/authentication/reset")
    public String setNewPassword(@ModelAttribute("code") UUID code,
                                 String password, String passwordConfirm) {
        if (password.equals(passwordConfirm)) {
            userService.setNewPasswordWithCode(code, password);
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
}
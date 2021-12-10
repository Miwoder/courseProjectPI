package org.bstu.govoronok.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bstu.govoronok.model.Auction;
import org.bstu.govoronok.model.Item;
import org.bstu.govoronok.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;


@Controller
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final UserService userService;
    private final AuctionService auctionService;
    private final PaymentService paymentService;
    private final ItemTypeService itemTypeService;
    private final PlaceService placeService;
    private final ItemService itemService;

    @GetMapping("/auctions/add/item")
    public String getFormForNewItem(Model model) {
        model.addAttribute("item", new Item());
        String itemTypeName = "Cars";
        model.addAttribute("itemTypeName", itemTypeName);
        model.addAttribute("itemTypes", itemTypeService.getAllItemTypes());
        return "/item/addNewItem";
    }

    @PostMapping("/auctions/add/item")
    public String addNewItem(@ModelAttribute("item") Item item, Principal principal, Model model) {
        item.setItemType(itemTypeService.getItemTypeByName("Cars"));
        item.setUser(userService.findByUsername(principal.getName()));
        itemService.saveItem(item);
        model.addAttribute("item", item);
        return "redirect:/auctions/add/auction";
    }

}
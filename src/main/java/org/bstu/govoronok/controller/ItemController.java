package org.bstu.govoronok.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bstu.govoronok.model.Item;
import org.bstu.govoronok.service.ItemService;
import org.bstu.govoronok.service.ItemTypeService;
import org.bstu.govoronok.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;


@Controller
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final UserService userService;
    private final ItemTypeService itemTypeService;
    private final ItemService itemService;

    @GetMapping("/auctions/add/item")
    public String getFormForNewItem(Model model) {
        model.addAttribute("item", new Item());
        model.addAttribute("itemTypes", itemTypeService.getAllItemTypes());
        return "/item/addNewItem";
    }

    @PostMapping("/auctions/add/item")
    public String addNewItem(@RequestParam("itemTypeName") String itemTypeName,
                             @ModelAttribute("item") Item item, Principal principal) {
        item.setUser(userService.findByUsername(principal.getName()));
        item.setItemType(itemTypeService.getItemTypeByName(itemTypeName));
        itemService.saveItem(item);
        return "redirect:/items/" + item.getId() + "/auction/add";
    }


}
package org.bstu.govoronok.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bstu.govoronok.model.Item;
import org.bstu.govoronok.service.ItemService;
import org.bstu.govoronok.service.ItemTypeService;
import org.bstu.govoronok.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
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
                             @Valid Item item, Errors errors, Principal principal, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("itemTypes", itemTypeService.getAllItemTypes());
            return "/item/addNewItem";
        }
        item.setUser(userService.findByUsername(principal.getName()));
        item.setItemType(itemTypeService.getItemTypeByName(itemTypeName));
        itemService.saveItem(item);

        return "redirect:/items/" + item.getId() + "/auction/add";
//        return "redirect:/items/" + item.getId() + "/image/add";
    }

    @GetMapping("/items/{itemId}/image/add")
    public String getFormForItemImage(@PathVariable("itemId") Long itemId, Model model) {
        model.addAttribute("itemId", itemId);
        return "/item/addItemImage";
    }

    @PostMapping("/items/{itemId}/image/add")
    public String addItemImage(@PathVariable("itemId") Long itemId,
                               @RequestParam("fileUpl") MultipartFile multipartFile) {
        Item item = itemService.getItemById(itemId).get();
        try {
            item.setImage(itemService.uploadFile(multipartFile));
            itemService.saveItem(item);
        } catch (IOException exception) {
            log.error("Image load error");
            return "/item/addNewItem";
        }
        return "redirect:/items/" + item.getId() + "/auction/add";
    }
}
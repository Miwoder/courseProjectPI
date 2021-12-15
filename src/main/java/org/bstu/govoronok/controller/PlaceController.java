package org.bstu.govoronok.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bstu.govoronok.model.Item;
import org.bstu.govoronok.service.*;
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
public class PlaceController {

    private final UserService userService;
    private final ItemTypeService itemTypeService;
    private final PlaceService placeService;
    private final ItemService itemService;

    @GetMapping("/places/")
    public String getAllPlaces(Model model) {
        model.addAttribute("places", placeService.getAllPlaces());
        return "/place/places";
    }
}
package com.mayosen.letipractice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/menu")
    public String menu() {
        return "menu";
    }
}

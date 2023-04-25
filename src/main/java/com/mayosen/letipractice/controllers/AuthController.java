package com.mayosen.letipractice.controllers;

import com.mayosen.letipractice.models.User;
import com.mayosen.letipractice.services.UserService;
import com.mayosen.letipractice.services.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final UserService userService;
    private final SecurityService securityService;

    @Autowired
    public AuthController(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userForm", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("userForm") User user) {
        userService.create(user);
        securityService.autoLogin(user.getLogin(), user.getPassword());
        return "redirect:/welcome";
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }
        if (error != null) {
            model.addAttribute("error", "Your username and password is invalid.");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }
        return "login";
    }

    @GetMapping({"/", "/welcome"})
    public String welcome() {
        return "welcome";
    }
}

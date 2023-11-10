package com.example.statki.controller;

import com.example.statki.service.UserService;
import com.example.statki.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class StatkiController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register.html";
    }

    @PostMapping("/register")
    public String registerUser(User user, Model model) {
        if (userService.isUserEmailUnique(user.getEmail())) {
            userService.saveUser(user);
            model.addAttribute("message", "Registration successful!");
            return "redirect:/login.html";
        } else {
            model.addAttribute("error", "User with this email already exists. Please use a different email.");
            return "register.html";
        }
    }

    /*@PostMapping("/login")
    public String loginUser(User user, Model model) {
        if (userService.isValidUser(user)) {
            return "redirect:/index";
        } else {
            model.addAttribute("error", "Invalid credentials. Please try again.");
            return "login.html";
        }
    }*/
}

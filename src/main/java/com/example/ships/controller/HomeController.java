package com.example.ships.controller;

import com.example.ships.model.Role;
import com.example.ships.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
            model.addAttribute("username", username);
        }return "home";
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
            model.addAttribute("username", username);
        }

        return "home";
    }

    @GetMapping("/admin")
    public String admin(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
            model.addAttribute("username", username);
        }

        Set<Role> roles = (Set<Role>) session.getAttribute("roles");
        if (roles != null) {
            System.out.println("Roles: " + roles);
            model.addAttribute("roles", roles);
        }

        return "admin";
    }
}


package com.example.ships.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        System.out.println("czy admin w /home " + isAdmin);
        if (username != null) {
            model.addAttribute("username", username);
            model.addAttribute("isAdmin", isAdmin);
        }
        return "index";
    }

    @GetMapping("/old_home")
    public String index3(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        System.out.println("czy admin w /home " + isAdmin);
        if (username != null) {
            model.addAttribute("username", username);
            model.addAttribute("isAdmin", isAdmin);
        }
        return "home";
    }

    @GetMapping("/home2")
    public String index2(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        System.out.println("czy admin w /home " + isAdmin);
        if (username != null) {
            model.addAttribute("username", username);
            model.addAttribute("isAdmin", isAdmin);
        }
        return "home_v2";
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        String email = (String) session.getAttribute("email");
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        System.out.println("czy admin w /home " + isAdmin);
        if (username != null) {
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            model.addAttribute("isAdmin", isAdmin);
        }
        return "userProfile";
    }

    @GetMapping("/admin")
    public String admin(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (username != null) {
            model.addAttribute("username", username);
            model.addAttribute("isAdmin", isAdmin);
        }

        return "admin";
    }

    @GetMapping("/placeholder")
    public String placeholder(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        System.out.println("czy admin w /home " + isAdmin);
        if (username != null) {
            model.addAttribute("username", username);
            model.addAttribute("isAdmin", isAdmin);
        }
        return "placeholder";
    }

    @GetMapping("/about_us")
    public String about_us(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        System.out.println("czy admin w /home " + isAdmin);
        if (username != null) {
            model.addAttribute("username", username);
            model.addAttribute("isAdmin", isAdmin);
        }
        return "about_us";
    }

    @GetMapping("/contact")
    public String contact(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        System.out.println("czy admin w /home " + isAdmin);
        if (username != null) {
            model.addAttribute("username", username);
            model.addAttribute("isAdmin", isAdmin);
        }
        return "contact";
    }

    @GetMapping("/service_offer")
    public String service_offer(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        System.out.println("czy admin w /home " + isAdmin);
        if (username != null) {
            model.addAttribute("username", username);
            model.addAttribute("isAdmin", isAdmin);
        }
        return "service_offer";
    }
}


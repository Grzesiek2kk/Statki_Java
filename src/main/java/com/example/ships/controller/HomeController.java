package com.example.ships.controller;

import com.example.ships.model.Role;
import com.example.ships.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.swing.text.StyledEditorKit;
import java.util.Set;

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
        return "home";
    }

    @GetMapping("/test")
    public String index3(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        System.out.println("czy admin w /home " + isAdmin);
        if (username != null) {
            model.addAttribute("username", username);
            model.addAttribute("isAdmin", isAdmin);
        }
        return "index";
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
}


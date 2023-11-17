package com.example.statki.controller;

import com.example.statki.service.UserService;
import com.example.statki.model.User;
import com.example.statki.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class StatkiController {

    @Autowired
    private HttpServletRequest request;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public StatkiController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/")
    public String index() {
        return "home";
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
            model.addAttribute("username", username);
        }

        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User user, Model model) {
        if (userService.isUserEmailUnique(user.getEmail())) {
            userService.saveUser(user);
            model.addAttribute("message", "Registration successful!");
            return "redirect:/login";
        } else {
            model.addAttribute("error", "User with this email already exists. Please use a different email.");
            return "register";
        }
    }

    @PostMapping("/login")
    public String loginUser(User user, Model model, HttpServletRequest request) {
        if (userService.isValidUser(user)) {
            User loggedInUser = userService.getUserByEmail(user.getEmail());
            if (loggedInUser != null) {
                String token = jwtUtil.generateToken(loggedInUser.getEmail());

                HttpSession session = request.getSession();
                session.setAttribute("username", loggedInUser.getUsername());
                session.setAttribute("token", token);

                return "redirect:/home";
            }
        }
        model.addAttribute("error", "Invalid credentials. Please try again.");
        return "login";
    }
}
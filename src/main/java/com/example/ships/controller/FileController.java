package com.example.ships.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.boot.Banner;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
public class FileController {

    @GetMapping("/importShipJsonForm")
    public String importShipJson(Model model, HttpSession session)
    {
        String username = (String) session.getAttribute("username");
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (username != null) {
            model.addAttribute("username", username);
            model.addAttribute("isAdmin", isAdmin);
        }
        return "importJson";
    }

    @PostMapping("/importShipJson")
    public String importShip(HttpServletRequest request,
                             RedirectAttributes redirectAttributes,
                             @RequestPart("fileShips") MultipartFile file)
    {

        System.out.println("File received: " + file.getOriginalFilename());
        redirectAttributes.addFlashAttribute("successMessage", "Plik został pomyślnie przesłany");
        return "redirect:/importShipJsonForm";

    }
}

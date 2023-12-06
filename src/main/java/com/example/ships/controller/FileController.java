package com.example.ships.controller;

import com.example.ships.model.Ship;
import com.example.ships.repo.ShipRepository;
import com.example.ships.service.FileService;
import com.example.ships.service.ShipService;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


@Controller
public class FileController {

    private final FileService _fileService;
    private final ShipService _shipService;
    private final ShipRepository _shipRepository;

    @Autowired
    public FileController(FileService fileService, ShipService shipService, ShipRepository shipRepository) {
        _fileService = fileService;
        _shipService = shipService;
        _shipRepository = shipRepository;
    }

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
                             @RequestPart("fileShips") MultipartFile file,
                             HttpSession session) throws IOException {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please select a file to upload.");
            return "redirect:/importShipJsonForm";
        }

        try {
            List<Ship> ships = _fileService.readShipFromJson(file);
            Long user_id = (Long) session.getAttribute("user_id");
            if (ships != null) {
                List<Ship> skipped = _shipService.saveAllShips(ships, user_id);
                if (!skipped.isEmpty()) {
                    if (ships.size() == skipped.size()) {
                        redirectAttributes.addFlashAttribute("errorMessage", "Import zakonczyl sie niepowodzeniem");
                    } else {
                        redirectAttributes.addFlashAttribute("successMessage", "Import zakonczył sie powodzeniem. Liczba pomietych statków: " + skipped.size());
                    }
                } else {
                    redirectAttributes.addFlashAttribute("successMessage", "Import zakonczył sie powodzeniem, wszytskie statki zostały zaimportowane");
                }
            }
        }catch (IOException | JsonParseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Plik json jest nieprawidlowy");
        }

        return "redirect:/importShipJsonForm";
    }

}

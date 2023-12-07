package com.example.ships.controller;

import com.example.ships.model.Ship;
import com.example.ships.repo.ShipRepository;
import com.example.ships.service.FileService;
import com.example.ships.service.ShipService;
import com.example.ships.service.UserService;
import com.google.gson.JsonParseException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
public class FileController {

    private final FileService _fileService;
    private final ShipService _shipService;
    private final UserService _userService;
    private final ShipRepository _shipRepository;

    @Autowired
    public FileController(FileService fileService, ShipService shipService, ShipRepository shipRepository, UserService userService) {
        _fileService = fileService;
        _shipService = shipService;
        _userService = userService;
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
        if(isAdmin)
        {
            return "importJson";
        }
        else
        {
            return "redirect:/";
        }

    }

    @PostMapping("/importShipJson")
    public String importToJsonFile(HttpServletRequest request,
                             RedirectAttributes redirectAttributes,
                             @RequestPart("fileShips") MultipartFile file,
                             HttpSession session) throws IOException
    {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please select a file to upload.");
            return "redirect:/importShipJsonForm";
        }

        try {
            List<Ship> shipsUnfilter = _fileService.readShipFromJson(file);

            Long user_id = (Long) session.getAttribute("user_id");


            if(shipsUnfilter.isEmpty())
            {
                redirectAttributes.addFlashAttribute("errorMessage", "Plik json jest pusty");
            }
            else
            {
                List<Ship> ships = _fileService.filterNull(shipsUnfilter);
                if(ships.isEmpty())
                {
                    redirectAttributes.addFlashAttribute("errorMessage", "Daty i czasy we wszystkich przybyciach są nieporawne");
                }
                else
                {
                    List<Ship> skipped = _shipService.saveAllShips(ships, user_id);
                    int unValidDateTime = shipsUnfilter.size()-ships.size();

                    if (!skipped.isEmpty() || !shipsUnfilter.isEmpty())
                    {
                        int importShips = ships.size()-skipped.size();
                        int skippedShips = skipped.size();
                        if(skipped.size()==ships.size())
                        {
                            redirectAttributes.addFlashAttribute("errorMessage", "Statki nie zostały zaimportowane. Błedne dane o statkach");
                        }
                        else
                        {
                            redirectAttributes.addFlashAttribute("successMessage", "Statki zostaly czesciowo zaimportowane.\nLiczba zaimportowanych statkow: "+importShips+"\n Liczba pomietych statków: " + (skippedShips+unValidDateTime));
                        }
                    } else
                    {
                        redirectAttributes.addFlashAttribute("successMessage", "Import zakonczył sie powodzeniem, wszytskie statki zostały zaimportowane");
                    }
                }
            }

        }catch (IOException | JsonParseException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Plik json jest nieprawidlowy");
        }

        return "redirect:/importShipJsonForm";
    }

    @GetMapping("/exportAll")
    public String exportAll(RedirectAttributes redirectAttributes) throws IOException {
        List<Ship> ships = _shipService.getAllShips();
        exportToJsonFile(ships,redirectAttributes);
        return "redirect:/show_all_ships";
    }

    @GetMapping("/exportOne/{id}")
    public String exportOne(@PathVariable Long id,RedirectAttributes redirectAttributes) throws IOException {
        List <Ship> ships = new ArrayList<>();
        Optional<Ship> ship = _shipRepository.findById(id);
        if(ship.isPresent())
        {
            ships.add(ship.get());
            exportToJsonFile(ships,redirectAttributes);
        }
        else
        {
            redirectAttributes.addFlashAttribute("errorMessage", "Statek nie istnieje");
            return "redirect:/show_all_ships";
        }
        return "redirect:/show_all_ships";
    }


    public String exportToJsonFile(List <Ship> ships, RedirectAttributes redirectAttributes) throws IOException {

        if(ships.isEmpty())
        {
            redirectAttributes.addFlashAttribute("errorMessage", "Brak statków do zaimportowania");
            return "redirect:/show_all_ships";
        }
        boolean isSuccess = _fileService.writeToJsonFile(ships);
        if(isSuccess)
        {
            redirectAttributes.addFlashAttribute("successMessage", "Eksportowanie pliku zakonczylo sie powodzeniem");
            return "redirect:/show_all_ships";
        }
        else
        {
            redirectAttributes.addFlashAttribute("errorMessage", "Eksportowanie pliku zakonczylo sie niepowodzeniem");
            return "redirect:/show_all_ships";
        }
    }
}

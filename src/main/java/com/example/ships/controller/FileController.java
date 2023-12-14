package com.example.ships.controller;

import com.example.ships.model.Ship;
import com.example.ships.repo.ShipRepository;
import com.example.ships.service.FileService;
import com.example.ships.service.ShipService;
import com.example.ships.service.UserService;
import com.google.gson.JsonParseException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Operation(summary = "Import JSON", description = "Zaimportuj dane statków z pliku JSON")
    public String importToJsonFile(HttpServletRequest request,
                             RedirectAttributes redirectAttributes,
                             @RequestPart("fileShips") MultipartFile file,
                             HttpSession session)
    {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please select a file to upload.");
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

                    if (!skipped.isEmpty() || shipsUnfilter.isEmpty())
                    {
                        int importShips = ships.size()-skipped.size();
                        int skippedShips = skipped.size();
                        if(skipped.size()==ships.size())
                        {
                            redirectAttributes.addFlashAttribute("errorMessage", "Statki nie zostały zaimportowane. Błedne dane o statkach");
                        }
                        else
                        {
                            redirectAttributes.addFlashAttribute("successMessage", "Statki zostaly czesciowo zaimportowane.\nLiczba zaimportowanych statkow: "+importShips+"\n Liczba pominietych statków: " + (skippedShips+unValidDateTime));
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

    @GetMapping("/importShipXmlForm")
    public String importShipXmlForm(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (username != null) {
            model.addAttribute("username", username);
            model.addAttribute("isAdmin", isAdmin);
        }
        if(isAdmin)
        {
            return "importXml";
        }
        else
        {
            return "redirect:/";
        }
    }

    @PostMapping("/importShipXml")
    @Operation(summary = "Import XML", description = "Zaimportuj dane statków z pliku XML")
    public String importShipXml(HttpServletRequest request,
                                RedirectAttributes redirectAttributes,
                                @RequestPart("fileShips") MultipartFile file,
                                HttpSession session) throws IOException {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please select a file to upload.");
            return "redirect:/importShipXmlForm";
        }

        try {
            List<Ship> shipsUnfilter = _fileService.readShipFromXml(file);

            Long user_id = (Long) session.getAttribute("user_id");

            if (shipsUnfilter.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Plik xml jest pusty");
            } else {
                List<Ship> ships = _fileService.filterNull(shipsUnfilter);
                if (ships.isEmpty()) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Daty i czasy we wszystkich przybyciach są nieporawne");
                } else {
                    List<Ship> skipped = _shipService.saveAllShips(ships, user_id);
                    int unValidDateTime = shipsUnfilter.size() - ships.size();

                    if (!skipped.isEmpty() || shipsUnfilter.isEmpty()) {
                        int importShips = ships.size() - skipped.size();
                        int skippedShips = skipped.size();
                        if (skipped.size() == ships.size()) {
                            redirectAttributes.addFlashAttribute("errorMessage", "Statki nie zostały zaimportowane. Błedne dane o statkach");
                        } else {
                            redirectAttributes.addFlashAttribute("successMessage", "Statki zostaly czesciowo zaimportowane.\nLiczba zaimportowanych statkow: "+importShips+"\n Liczba pomietych statków: " + (skippedShips+unValidDateTime));
                        }
                    } else {
                        redirectAttributes.addFlashAttribute("successMessage", "Import zakonczył sie powodzeniem, wszytskie statki zostały zaimportowane");
                    }
                }
            }

        } catch (IOException  e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Plik XML jest nieprawidłowy");
        }

        return "redirect:/importShipXmlForm";
    }


    @GetMapping("/exportXmlAllShip")
    @Operation(summary = "Export XML", description = "Wyeksportuj dane statków do pliku XML")
    public String exportAll(RedirectAttributes redirectAttributes) throws IOException {
        List<Ship> ships = _shipService.getAllShips();
        _fileService.writeToXmlFile(ships,redirectAttributes);
        return "redirect:/show_all_ships";
    }

    @GetMapping("/exportXmlOneShip/{id}")
    @Operation(summary = "Export XML OneShip", description = "Wyeksportuj dane statku do pliku XML")
    public String exportOne(@PathVariable Long id,RedirectAttributes redirectAttributes) throws IOException {
        List <Ship> ships = new ArrayList<>();
        Optional<Ship> ship = _shipRepository.findById(id);
        if(ship.isPresent())
        {
            ships.add(ship.get());
            _fileService.writeToXmlFile(ships,redirectAttributes);
            return "redirect:/show_all_ships";
        }
        else
        {
            redirectAttributes.addFlashAttribute("errorMessage", "Statek nie istnieje");
            return "redirect:/show_all_ships";
        }

    }
    @GetMapping("/exportJsonAllShips")
    @Operation(summary = "Export JSON", description = "Wyeksportuj dane statków do pliku JSON")
    public String exportAllJson(RedirectAttributes redirectAttributes) throws IOException {
        List<Ship> ships = _shipService.getAllShips();
        _fileService.exportToJsonFile(ships,redirectAttributes);
        return "redirect:/show_all_ships";
    }

    @GetMapping("/exportJsonOneShip/{id}")
    @Operation(summary = "Export JSON OneShip", description = "Wyeksportuj dane statku do pliku JSON")
    public String exportOneJson(@PathVariable Long id,RedirectAttributes redirectAttributes) throws IOException {
        List <Ship> ships = new ArrayList<>();
        Optional<Ship> ship = _shipRepository.findById(id);
        if(ship.isPresent())
        {
            ships.add(ship.get());
            _fileService.exportToJsonFile(ships,redirectAttributes);
            return "redirect:/show_all_ships";
        }
        else
        {
            redirectAttributes.addFlashAttribute("errorMessage", "Statek nie istnieje");
            return "redirect:/show_all_ships";
        }
    }

}

package com.example.ships.controller;

import com.example.ships.model.Ship;
import com.example.ships.repo.ShipRepository;
import com.example.ships.service.ShipService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class ShipController {

    private final ShipRepository shipRepository;
    private final ShipService shipService;

    @Autowired
    public ShipController(ShipRepository shipRepository, ShipService shipService) {
        this.shipRepository = shipRepository;
        this.shipService = shipService;
    }

    @GetMapping("/addShip")
    public String showAddShipForm(Model model) {
        model.addAttribute("newShip", new Ship());
        return "addShip";
    }

    @Transactional
    @PostMapping("/addShip")
    public String addShip(
            @Valid @ModelAttribute("newShip") Ship newShip,
            BindingResult bindingResult,
            Model model,
            @RequestParam("arrivalDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate arrivalDateParam,
            @RequestParam("arrivalTime") @DateTimeFormat(pattern = "HH:mm:ss") LocalTime arrivalTimeParam) {

        if (!shipService.validationTimeDependsOnDate(newShip.getArrivalDate(), newShip.getArrivalTime())) {
            bindingResult.rejectValue("arrivalTime", "ship.invalidTime", "Dla wybranej daty ten czas wykracza w przyszłość");
        }

        if (!shipService.validationDate(newShip.getArrivalDate())) {
            bindingResult.rejectValue("arrivalDate", "ship.invalidDate", "Wybrana data jest z przyszłości");
        }

        if (bindingResult.hasErrors()) {
            model.addAllAttributes(bindingResult.getModel());
            return "addShip";
        }

        if (shipService.duplicateArrival(newShip.getShipName(), newShip.getArrivalDate(), newShip.getArrivalTime())) {
            model.addAttribute("duplicateArrivalError", "Statek o podanej nazwie, dacie i godzinie już istnieje");
            return "addShip"; // Return the view without saving the ship
        }

        try {
            shipService.saveShip(newShip);
            model.addAttribute("successMessage", "Statek dodany pomyślnie.");
        } catch (DataAccessException e) {
            model.addAttribute("errorMessage", "Wystąpił błąd podczas dodawania statku do bazy danych.");
            return "addShip";
        }

        return "addShip";
    }

    @GetMapping("/showArrivalShips")
    public String showArrivalShips(Model model)
    {
        List<Ship> ships = shipService.getAllShips();
        model.addAttribute("ships", ships);
        return "showArrivalShips";
    }

}

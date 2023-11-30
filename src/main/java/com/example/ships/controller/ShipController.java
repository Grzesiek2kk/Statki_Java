package com.example.ships.controller;

import com.example.ships.model.Ship;
import com.example.ships.model.User;
import com.example.ships.repo.ShipRepository;
import com.example.ships.service.ShipService;
import com.example.ships.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class ShipController {

    private final ShipService shipService;
    private final ShipRepository shipRepository;
    private final UserService userService;

    @Autowired
    public ShipController(ShipService shipService, ShipRepository shipRepository, UserService userService) {
        this.shipService = shipService;
        this.shipRepository = shipRepository;
        this.userService = userService;
    }

    @GetMapping("/addShip")
    public String showAddShipForm(Model model, HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("user_id");
        String username = (String) session.getAttribute("username");
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("username", username);
            User loggedInUser = userService.getUserById(userId);
            if (loggedInUser != null)
            {
                model.addAttribute("newShip", new Ship());
                System.out.println(userId);
                return "addShip";
            }
            else
            {
                model.addAttribute("errorMessage", "Niezalogowany użytkownik nie moze wprowadzać nowych przypłynięć!");
                return "home";
            }
    }

    @Transactional
    @PostMapping("/addShip")
    public String addShip(
            @Valid @ModelAttribute("newShip") Ship newShip,
            BindingResult bindingResult,
            Model model,
            @RequestParam("arrivalDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate arrivalDateParam,
            @RequestParam("arrivalTime") @DateTimeFormat(pattern = "HH:mm:ss") LocalTime arrivalTimeParam,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes)
    {
        HttpSession session = request.getSession();
        System.out.println(session.getAttribute("user_id"));
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
            return "addShip";
        }

            try {
                User user = userService.getUserById((Long)session.getAttribute("user_id"));
                newShip.setUser(user);
                shipService.saveShip(newShip);
            } catch (Exception e) {
                model.addAttribute("errorMessage", "Wystąpił błąd podczas dodawania statku do bazy danych.");
                return "addShip";
            }

        redirectAttributes.addFlashAttribute("successMessage", "Statek dodany pomyślnie.");
        return "redirect:/showArrivalShips";
    }

    @GetMapping("/showArrivalShips")
    public String showArrivalShips(Model model, HttpSession session)
    {
        String username = (String) session.getAttribute("username");
        Boolean isAdmin = (Boolean) session.getAttribute("isAdmin");
        if (username != null) {
            model.addAttribute("username", username);
            model.addAttribute("isAdmin", isAdmin);
        }

        List<Ship> ships = shipService.getAllShips();
        model.addAttribute("ships", ships);
        return "showArrivalShips";
    }

}

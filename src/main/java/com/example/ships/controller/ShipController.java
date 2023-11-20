package com.example.ships.controller;

import com.example.ships.model.Ship;
import com.example.ships.repo.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.ships.controller.ShipController;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
public class ShipController {

    private final ShipRepository shipRepository;

    @Autowired
    public ShipController(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @GetMapping("/addShipForm")
    public String index() {
        System.out.println("form");
        return "addShip";
    }

    @Transactional
    @PostMapping("/addShip")
    public String addShip(
            @RequestParam("arrival_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String arrivalDateStr,
            @RequestParam("arrival_time") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) String arrivalTimeStr,
            @RequestParam("gross_capacity") double grossCapacity,
            @RequestParam("length") double length,
            @RequestParam("agent") String agent,
            @RequestParam("mooring place") String place,
            @RequestParam("initial_port_code") String initial_port_code,
            @RequestParam("ship_name") String ship_name) {

        System.out.println("im here");
        try
        {
            Ship newShip = new Ship();
            newShip.final_port_code = "PLSWI";
            newShip.arrival_date = LocalDate.parse(arrivalDateStr);
            newShip.arrival_time = LocalTime.parse(arrivalTimeStr);
            newShip.gross_capacity = grossCapacity;
            newShip.length = length;
            newShip.agent = agent;
            newShip.mooring_place = place;
            newShip.initial_port_code = initial_port_code;
            newShip.ship_name = ship_name;

            System.out.println("New Ship: " + newShip.toString());
            shipRepository.save(newShip);

        }catch (Exception e)
        {
            e.printStackTrace();
        }


        return "addShip";
    }
}

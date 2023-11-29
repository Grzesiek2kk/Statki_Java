package com.example.ships.service;
import com.example.ships.model.Ship;
import com.example.ships.repo.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ShipService {

    @Autowired
    private ShipRepository shipRepository;

    @Transactional
    public void saveShip(Ship newShip)
    {
        shipRepository.save(newShip);
    }
    public boolean duplicateArrival(String shipName, LocalDate arrivalDate, LocalTime arrivalTime) {
        List<Ship> duplicates = shipRepository.findByShipNameAndArrivalDateAndArrivalTime(shipName, arrivalDate, arrivalTime);
        return !duplicates.isEmpty();
    }

    public boolean validationDate(LocalDate arrivalDate)
    {
        LocalDate now = LocalDate.now();

        if(arrivalDate.isAfter(now))
        {
            return false;
        }
        return true;
    }
    public boolean validationTimeDependsOnDate(LocalDate arrivalDate, LocalTime arrivalTime) {
        LocalDate now = LocalDate.now();
        LocalTime nowT = LocalTime.now();

        if (arrivalDate.isEqual(now)) {
            return !arrivalTime.isAfter(nowT);
        }

        return true;
    }

    public List<Ship> getAllShips() {
        return shipRepository.findAll();
    }
}

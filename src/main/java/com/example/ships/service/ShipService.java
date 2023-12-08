package com.example.ships.service;
import com.example.ships.model.Ship;
import com.example.ships.model.User;
import com.example.ships.repo.ShipRepository;
import jakarta.validation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShipService {

    private final ShipRepository shipRepository;
    private final UserService userService;

    @Autowired
    public ShipService(ShipRepository shipRepository, UserService userService) {
        this.shipRepository = shipRepository;
        this.userService = userService;
    }

    @Transactional
    public void saveShip(Ship newShip)
    {
        shipRepository.save(newShip);
    }
    @Transactional
    public List<Ship> saveAllShips(List<Ship> ships, Long user_id)
    {
        User u = userService.getUserById(user_id);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        List<Ship> invalidShips = new ArrayList<>();


        for (Ship s : ships)
            {
                boolean isDuplicateArrival = duplicateArrival(s);
                boolean isValidateDate = validationDate(s);
                boolean isValidateTimeDependsOnDate = validationTimeDependsOnDate(s);

                System.out.println(isDuplicateArrival);
                System.out.println(s.shipName);

                if (isDuplicateArrival || isValidateDate || isValidateTimeDependsOnDate) {
                    System.out.println(s.shipName);
                    invalidShips.add(s);
                } else
                {
                    Set<ConstraintViolation<Ship>> violations = validator.validate(s);

                    if (!violations.isEmpty())
                    {
                        invalidShips.add(s);
                    } else
                    {
                        s.setUser(u);
                        shipRepository.save(s);
                    }
                }
            }
        return invalidShips;
    }

    public boolean duplicateArrival(Ship s) {
        List<Ship> duplicates = shipRepository.findByShipNameAndArrivalDateAndArrivalTime(s.getShipName(), s.getArrivalDate(), s.getArrivalTime());
        return !duplicates.isEmpty();
    }

    public boolean duplicateArrivalEdit(Ship s)
    {
        List<Ship> duplicates = shipRepository.findByShipNameAndArrivalDateAndArrivalTime(s.shipName, s.arrivalDate, s.arrivalTime);

        if(duplicates.size()==1)
        {
            return false;
        }
        return true;
    }

    public boolean validationDate(Ship s)
    {
        LocalDate now = LocalDate.now();

        if(s.arrivalDate.isAfter(now))
        {
            return true;
        }
        return false;
    }
    public boolean validationTimeDependsOnDate(Ship s) {
        LocalDate now = LocalDate.now();
        LocalTime nowT = LocalTime.now();

        if (s.arrivalDate.isEqual(now)) {
            if(s.arrivalTime.isAfter(nowT)) {
                return true;
            }
        }
        return false;
    }

    public List<Ship> getAllShips()
    {
        return shipRepository.findAll();
    }

}

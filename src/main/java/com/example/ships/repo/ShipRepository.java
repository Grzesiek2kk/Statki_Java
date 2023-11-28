package com.example.ships.repo;

import com.example.ships.model.Ship;
import com.example.ships.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ShipRepository extends JpaRepository<Ship, Long> {
    List<Ship> findByShipNameAndArrivalDateAndArrivalTime(String shipName, LocalDate arrivalDate, LocalTime arrivalTime);
}
package com.example.ships.repo;

import com.example.ships.model.Ship;
import com.example.ships.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipRepository extends JpaRepository<Ship, Long> {
    
}

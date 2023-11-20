package com.example.ships.service;

import com.example.ships.repo.ShipRepository;

public class ShipService {

    private final ShipRepository shipRepository;

    public ShipService(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }
}

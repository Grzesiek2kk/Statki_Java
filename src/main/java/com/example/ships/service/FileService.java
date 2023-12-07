package com.example.ships.service;

import com.example.ships.adapter.LocalDateAdapter;
import com.example.ships.adapter.LocalTimeAdapter;
import com.example.ships.model.Ship;
import com.example.ships.repo.ShipRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Service
public class FileService
{
    @Autowired
    private ShipRepository shipRepository;

    public List<Ship> readShipFromJson(MultipartFile file) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .create();

        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            List<Ship> ships = gson.fromJson(reader, new TypeToken<List<Ship>>() {}.getType());
            return ships;
        }
    }
}

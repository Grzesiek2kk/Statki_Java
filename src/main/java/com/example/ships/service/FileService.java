package com.example.ships.service;

import com.example.ships.adapter.LocalDateAdapter;
import com.example.ships.adapter.LocalTimeAdapter;
import com.example.ships.model.Ship;
import com.example.ships.repo.ShipRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDate.*;


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

    public List<Ship> filterNull(List<Ship> ships)
    {
        List<Ship> filteredShips = ships.stream()
                .filter(ship -> ship != null && ship.getArrivalDate() != null && ship.getArrivalTime() != null)
                .collect(Collectors.toList());
        return filteredShips;
    }

    public List<Ship> parseDate(List<Ship> ships) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Ship ship : ships) {
            String timeString = String.valueOf(ship.getArrivalTime());
            LocalTime arrivalTime = LocalTime.parse(timeString, timeFormatter);

            String dateString = String.valueOf(ship.getArrivalDate());
            LocalDate arrivalDate = parse(dateString, inputFormatter);
            String formattedDate = arrivalDate.format(outputFormatter);

            ship.setArrivalDate(parse(formattedDate, outputFormatter));
            ship.setArrivalTime(arrivalTime);
        }

        return ships;
    }

    public boolean writeToJsonFile(List<Ship> ships) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .create();

        List<Ship> newShips = parseDate(ships);
        System.out.println("Number of ships: " + newShips.size());

        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH-mm-ss");

        String formattedDate = currentDate.format(dateFormatter);
        String formattedTime = currentTime.format(timeFormatter);

        String fileName = "exportedShips_"+formattedDate + "_" + formattedTime + ".json";
        Path filePath = Paths.get("src/main/resources/exportFiles", fileName);

        if(newShips.isEmpty())
        {
            return false;
        }

        try (FileWriter writer = new FileWriter(filePath.toAbsolutePath().toString()))
        {
            gson.toJson(newShips, writer);
            writer.flush();
        } catch (JsonIOException | IOException e)
        {
            return false;
        }
        return true;
    }


}

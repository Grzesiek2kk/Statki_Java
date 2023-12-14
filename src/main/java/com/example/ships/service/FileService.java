package com.example.ships.service;

import com.example.ships.adapter.LocalDateAdapter;
import com.example.ships.adapter.LocalTimeAdapter;
import com.example.ships.model.Ship;
import com.example.ships.repo.ShipRepository;
import com.example.ships.util.LocalDateDeserializer;
import com.example.ships.util.LocalTimeDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import static java.time.LocalDate.*;


import static java.time.LocalDate.*;


@Service
public class FileService
{
    @Autowired
    private ShipRepository shipRepository;
    private ShipService shipService;

    public List<Ship> readShipFromJson(MultipartFile file) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .create();

        try (Reader reader = new InputStreamReader(file.getInputStream()))
        {
            List<Ship> ships = gson.fromJson(reader, new TypeToken<List<Ship>>() {}.getType());
            return ships;
        }
    }

    public List<Ship> filterNull(List<Ship> ships)
    {
        for(Ship s: ships)
        {
            System.out.println(s);
        }

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

    public void exportToJsonFile(List <Ship> ships, RedirectAttributes redirectAttributes) throws IOException {

        if(ships.isEmpty())
        {
            redirectAttributes.addFlashAttribute("errorMessage", "Brak statków do zaimportowania");
        }
        boolean isSuccess = writeToJsonFile(ships);
        if(isSuccess)
        {
            redirectAttributes.addFlashAttribute("successMessage", "Eksportowanie pliku zakonczylo sie powodzeniem");
        }
        else
        {
            redirectAttributes.addFlashAttribute("errorMessage", "Eksportowanie pliku zakonczylo sie niepowodzeniem");

        }
    }


    public List<Ship> readShipFromXml(MultipartFile file) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        try (InputStream inputStream = file.getInputStream()) {
            List<Ship> ships = Arrays.asList(xmlMapper.readValue(inputStream, Ship[].class));
            System.out.println("Deserializacja XML udana. Liczba wczytanych statków: " + ships.size());
            return ships;
        }catch (Exception e) {
            System.err.println("Błąd podczas deserializacji danych XML: " + e.getMessage());
            throw new IOException("Błąd podczas deserializacji danych XML", e);
        }
    }

    public boolean writeToXmlFile(List<Ship> ships, RedirectAttributes redirectAttributes) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new JavaTimeModule());
        List<Ship> newShips = parseDate(ships);
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH-mm-ss");

        String formattedDate = currentDate.format(dateFormatter);
        String formattedTime = currentTime.format(timeFormatter);

        String fileName = "exportedShips_" + formattedDate + "_" + formattedTime + ".xml";
        Path filePath = Paths.get("src/main/resources/exportFiles", fileName);

        if (newShips.isEmpty()) {
            return false;
        }

        try (FileWriter writer = new FileWriter(filePath.toAbsolutePath().toString())) {
            xmlMapper.writeValue(writer, newShips);
            redirectAttributes.addFlashAttribute("successMessage", "Pomyślnie wyeksportowano statki do pliku XML");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Podczas eksportu wystąpił błąd");
            return false;
        }
        return true;
    }

}

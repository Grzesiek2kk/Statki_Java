package com.example.ships.controller;

import com.example.ships.adapter.LocalDateAdapter;
import com.example.ships.adapter.LocalTimeAdapter;
import com.example.ships.service.PortApiService;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@Controller
public class PortApiController
{
    private final WebClient webClient;
    private final PortApiService portApiService;

    Gson gson;


    public PortApiController(PortApiService portApiService)
    {
        this.portApiService = portApiService;
        this.webClient = WebClient.builder().baseUrl("https://port-api.com").build();
        this.gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .create();
    }


    @GetMapping("/portDetails/{port}")
    @Operation(summary = "Port Macierzysty", description = "Informacje o porcie macierzystym")
    public String getInitialPortDetails(@PathVariable String port, Model model, RedirectAttributes redirectAttributes)
    {
        String apiEndpoint = "/port/code/" + port;

        try
        {
            String apiResponse = webClient.get()
                    .uri(apiEndpoint)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            Map<String, Object> data = portApiService.convertStringToMap(apiResponse);
            model.addAttribute("port",data);

        } catch (WebClientResponseException ex) {

            HttpStatus statusCode = (HttpStatus) ex.getStatusCode();

            if (statusCode == HttpStatus.NOT_FOUND) {
                redirectAttributes.addFlashAttribute("errorMessage", "Kod portu nie został odnaleziony");
                return "redirect:/show_all_ships";
            } else if (statusCode.is5xxServerError()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Blad serwera");
                return "redirect:/show_all_ships";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Wystapil bład");
                return "redirect:/show_all_ships";
            }
        }

        return "portDetails";
    }
}

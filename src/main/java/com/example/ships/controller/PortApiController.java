package com.example.ships.controller;

import com.example.ships.adapter.LocalDateAdapter;
import com.example.ships.adapter.LocalTimeAdapter;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PortApiController
{
    private final WebClient webClient;
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .create();

    public PortApiController() {
        this.webClient = WebClient.builder().baseUrl("https://port-api.com").build();
    }

    public Map<String, Object> convertStringToMap(String jsonString) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(jsonString, new TypeToken<Map<String, Object>>(){}.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/portDetails/{port}")
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

            Map<String, Object> data = convertStringToMap(apiResponse);
            model.addAttribute("port",data);

        } catch (WebClientResponseException ex) {

            HttpStatus statusCode = (HttpStatus) ex.getStatusCode();

            if (statusCode == HttpStatus.NOT_FOUND) {
                redirectAttributes.addFlashAttribute("error", "Kod portu nie został odnaleziony");
                return "redirect:/showArrivalShips";
            } else if (statusCode.is5xxServerError()) {
                redirectAttributes.addFlashAttribute("error", "Blad serwera");
                return "redirect:/showArrivalShips";
            } else {
                redirectAttributes.addFlashAttribute("error", "Wystapil bład");
                return "redirect:/showArrivalShips";
            }
        }

        return "portDetails";
    }
}

package com.example.ships.integration;


import com.example.ships.controller.PortApiController;
import com.example.ships.model.Ship;
import com.example.ships.model.User;
import com.example.ships.repo.ShipRepository;
import com.example.ships.repo.UserRepository;
import com.example.ships.service.PortApiService;
import com.example.ships.service.ShipService;
import com.example.ships.service.UserService;
import com.example.ships.util.JwtUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PortApiController.class)
public class PortApiControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PortApiService portApiService;

    private User user;
    private Ship ship;

    @BeforeEach
    void setUp()
    {
        ship = new Ship(1L, "PLSWI", LocalDate.now(), LocalTime.now(), "SEYST", "SKANIA", "BAHAMAS", 20.2, 10.2, "BALTIC SHIPPING AGENCY Ltd. Szczecin", "Portowców-Pirs", user);
    }

    @Test
    public void getInitialPortDetails_ReturnsPortDetailsPage() throws Exception
    {

        String apiResponse = "{\n" +
                "  \"type\": \"FeatureCollection\",\n" +
                "  \"features\": [\n" +
                "    {\n" +
                "      \"type\": \"Feature\",\n" +
                "      \"geometry\": {\n" +
                "        \"type\": \"Point\",\n" +
                "        \"coordinates\": [\n" +
                "          13.81666667,\n" +
                "          55.41666667\n" +
                "        ]\n" +
                "      },\n" +
                "      \"properties\": {\n" +
                "        \"id\": 86038,\n" +
                "        \"country\": {\n" +
                "          \"code\": \"SE\",\n" +
                "          \"name\": \"Sweden\",\n" +
                "          \"continent\": \"Europe\",\n" +
                "          \"wikipedia\": \"https://en.wikipedia.org/wiki/Sweden\"\n" +
                "        },\n" +
                "        \"name\": \"Ystad\",\n" +
                "        \"source\": \"unlocode\",\n" +
                "        \"distance\": null,\n" +
                "        \"match_relevance\": {\n" +
                "          \"code\": 1,\n" +
                "          \"country\": null,\n" +
                "          \"levenshtein\": null,\n" +
                "          \"ts_rank\": null,\n" +
                "          \"trgm_similarity\": null,\n" +
                "          \"skipped_chunks\": 0\n" +
                "        },\n" +
                "        \"match_level\": 1,\n" +
                "        \"region\": {\n" +
                "          \"code\": \"SE-M\",\n" +
                "          \"local_code\": \"M\",\n" +
                "          \"name\": \"Skåne län\",\n" +
                "          \"wikipedia\": \"https://en.wikipedia.org/wiki/Skåne_län\"\n" +
                "        },\n" +
                "        \"functions\": [\n" +
                "          \"seaport\",\n" +
                "          \"rail_terminal\",\n" +
                "          \"road_terminal\"\n" +
                "        ],\n" +
                "        \"iata\": null,\n" +
                "        \"status\": \"Approved by competent national government agency\",\n" +
                "        \"un_locode\": \"SEYST\",\n" +
                "        \"type\": \"trade_or_transport_location\"\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"properties\": {}\n" +
                "}";

        Map<String, Object> mockedPortDetails = new Gson().fromJson(apiResponse, new TypeToken<Map<String, Object>>() {}.getType());
        when(portApiService.convertStringToMap(any(String.class))).thenReturn(mockedPortDetails);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/portDetails/{port}", ship.getInitialPortCode()));

        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("port"))
                .andExpect(MockMvcResultMatchers.view().name("portDetails"));
    }
}

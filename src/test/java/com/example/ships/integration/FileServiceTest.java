package com.example.ships.integration;

import com.example.ships.model.Ship;
import com.example.ships.model.User;
import com.example.ships.repo.ShipRepository;
import com.example.ships.repo.UserRepository;
import com.example.ships.service.FileService;
import com.example.ships.service.ShipService;
import com.example.ships.service.UserService;
import com.example.ships.util.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class FileServiceTest {

    private ShipService shipService;
    private FileService fileService;
    private ShipRepository shipRepository;
    private UserService userService;
    private UserRepository userRepository;
    private User user;
    private Ship ship;
    private BCryptPasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    private MultipartFile multipartFile;
    @BeforeEach
    void setUp()
    {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(BCryptPasswordEncoder.class);
        jwtUtil = mock(JwtUtil.class);
        userService = new UserService(userRepository, passwordEncoder, jwtUtil);
        shipRepository = mock(ShipRepository.class);
        shipService = new ShipService(shipRepository,userService);
        user = mock(User.class);
        ship = new Ship(1L, "PLSWI", LocalDate.of(2005,1,1), LocalTime.of(1,1), "SEYST", "SKANIA", "BAHAMAS", 20.2, 10.2, "BALTIC SHIPPING AGENCY Ltd. Szczecin", "Portowców-Pirs", user);
        fileService = mock(FileService.class);
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("importFiles/statkiDoImportuTest.json");
        multipartFile = mock(MultipartFile.class);
        try {
            when(multipartFile.getInputStream()).thenReturn(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @Test
    public void readFileFromJsonAndSaveIntoDatabase() throws IOException {
        when(fileService.readShipFromJson(any())).thenCallRealMethod();
        List<Ship> ships = fileService.readShipFromJson(multipartFile);

        when(shipRepository.saveAll(anyCollection())).thenAnswer(invocation -> {
            List<Ship> savedShips = new ArrayList<>(invocation.getArgument(0));
            for (Ship savedShip : savedShips) {
                savedShip.setId(1L);
            }
            return savedShips;
        });

        List<Ship> invalidShips = shipService.saveAllShips(ships, 1L);

        Assertions.assertTrue(invalidShips.isEmpty(), "There should be no invalid ships");
    }
}

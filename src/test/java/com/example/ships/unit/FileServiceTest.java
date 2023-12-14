package com.example.ships.unit;

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
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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


    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(BCryptPasswordEncoder.class);
        jwtUtil = mock(JwtUtil.class);
        userService = new UserService(userRepository, passwordEncoder, jwtUtil);
        shipRepository = mock(ShipRepository.class);
        shipService = new ShipService(shipRepository,userService);
        user = mock(User.class);
        ship = new Ship(1L, "PLSWI", LocalDate.of(2005,1,1), LocalTime.of(1,1), "SEYST", "SKANIA", "BAHAMAS", 20.2, 10.2, "BALTIC SHIPPING AGENCY Ltd. Szczecin", "Portowców-Pirs", user);
        fileService = mock(FileService.class);
    }

    @Test
    void testWriteJsonFileWhenFileIsValid() throws IOException {
        List<Ship> ships = new ArrayList<>();
        ships.add(ship);
        ships.add(ship);

        when(fileService.writeToJsonFile(ships)).thenReturn(true);

        boolean result = fileService.writeToJsonFile(ships);

        verify(fileService, times(1)).writeToJsonFile(eq(ships));

        Assertions.assertTrue(result);
    }

    @Test
    void testWriteJsonFileWhenFileIsEmpty() throws IOException {
        List<Ship> ships = new ArrayList<>();

        when(fileService.writeToJsonFile(ships)).thenReturn(false);

        boolean result = fileService.writeToJsonFile(ships);

        verify(fileService, times(1)).writeToJsonFile(eq(ships));

        Assertions.assertFalse(result);
    }

    @Test
    void testFilterNullWhenDateTimeIsNull()
    {
        List<Ship> ships = new ArrayList<>();
        Ship nullArrivalDate = ship;
        nullArrivalDate.setArrivalTime(null);

        List<Ship>filterShip = fileService.filterNull(ships);
        Ship[] shipArray = filterShip.toArray(new Ship[0]);

        Assertions.assertArrayEquals(new Ship[0], shipArray);
    }


}

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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDate.parse;
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
    private RedirectAttributes redirectAttributes;

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
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("importFiles/statkiDoImportuTest.json");
        multipartFile = mock(MultipartFile.class);
        try {
            when(multipartFile.getInputStream()).thenReturn(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


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

    @Test
    void testReadFromJsonFile() throws IOException {

        when(fileService.readShipFromJson(any())).thenCallRealMethod();
        List <Ship> ships = fileService.readShipFromJson(multipartFile);

        Assertions.assertEquals(5,ships.size());


    }

    @Test
    void testReadShipFromXml() throws IOException {
        InputStream inputStreamXml = getClass().getClassLoader().getResourceAsStream("importFiles/statki.xml");
        try {
            when(multipartFile.getInputStream()).thenReturn(inputStreamXml);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        when(fileService.readShipFromXml(any())).thenCallRealMethod();

        List<Ship> ships = fileService.readShipFromXml(multipartFile);

        Assertions.assertEquals(2, ships.size());
    }

    @Test
    void testWriteToXmlFile() throws IOException {

        List<Ship> ships = new ArrayList<>();
        ships.add(ship);
        ships.add(ship);

        when(fileService.writeToXmlFile(ships, redirectAttributes)).thenReturn(true);

        boolean result = fileService.writeToXmlFile(ships, redirectAttributes);

        verify(fileService, times(1)).writeToXmlFile(ships, redirectAttributes);

        Assertions.assertTrue(result);
    }

    @Test
    void testWriteXmlFileWhenFileIsEmpty() throws IOException {
        List<Ship> ships = new ArrayList<>();

        when(fileService.writeToXmlFile(ships, redirectAttributes)).thenReturn(false);

        boolean result = fileService.writeToXmlFile(ships, redirectAttributes);

        verify(fileService, times(1)).writeToXmlFile(ships, redirectAttributes);

        Assertions.assertFalse(result);
    }

}

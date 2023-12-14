package com.example.ships.unit;

import com.example.ships.model.Ship;
import com.example.ships.model.User;
import com.example.ships.repo.ShipRepository;
import com.example.ships.repo.UserRepository;
import com.example.ships.service.ShipService;
import com.example.ships.service.UserService;
import com.example.ships.util.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ShipServiceTest
{
    private ShipRepository shipRepository;
    private ShipService shipService;
    private UserService userService;
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    private User user;
    private Ship ship;

    @BeforeEach
    void setUp()
    {
        ship = new Ship(1L, "PLSWI", LocalDate.now(), LocalTime.now(), "SEYST", "SKANIA", "BAHAMAS", 20.2, 10.2, "BALTIC SHIPPING AGENCY Ltd. Szczecin", "Portowców-Pirs", user);
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(BCryptPasswordEncoder.class);
        jwtUtil = mock(JwtUtil.class);
        userService = new UserService(userRepository, passwordEncoder, jwtUtil);
        shipRepository = mock(ShipRepository.class);
        shipService = new ShipService(shipRepository,userService);

    }
    @Test
    void testValidationDateValidDate() {
        LocalDate currentDate = LocalDate.of(2015, 1, 1);
        ship.setArrivalDate(currentDate);
        boolean result = shipService.validationDate(ship);

        Assertions.assertFalse(result);
    }

    @Test
    void testValidationDateInValidDate() {
        LocalDate currentDate = LocalDate.of(2023, 12, 31);
        ship.setArrivalDate(currentDate);
        boolean result = shipService.validationDate(ship);

        Assertions.assertTrue(result);
    }

    @Test
    void testValidationTimeDependsOnDateInValidTime() {
        ship.setArrivalTime(LocalTime.now().plusMinutes(2));
        boolean result = shipService.validationTimeDependsOnDate(ship);

        Assertions.assertTrue(result);
    }

    @Test
    void testValidationTimeDependsOnDateValidTime() {
        ship.setArrivalTime(LocalTime.now().minusMinutes(2));
        boolean result = shipService.validationTimeDependsOnDate(ship);

        Assertions.assertFalse(result);
    }

    @Test
    void saveShipValid()
    {
        when(shipRepository.save(any(Ship.class))).thenAnswer(invocation -> {
            Ship savedShip = invocation.getArgument(0);
            savedShip.setId(1L);
            return savedShip;
        });

        shipService.saveShip(ship);

        Optional<Ship> s = shipRepository.findById(1L);
        if(s.isPresent())
        {
            Ship savedShip = s.get();
        }
        verify(shipRepository).save(argThat(shipArgument -> shipArgument.getId().equals(1L)));

        Assertions.assertNotNull(ship.getId(), "Zapisany statek powienien mieć przypisane ID");
    }

    @Test
    void testIsDuplicateShipsWhileIsDuplicate() {

        when(shipRepository.findByShipNameAndArrivalDateAndArrivalTime(ship.getShipName(), ship.getArrivalDate(), ship.getArrivalTime()))
                .thenReturn(Collections.singletonList(ship));

        boolean isDuplicate = shipService.duplicateArrival(ship);

        Assertions.assertTrue(isDuplicate, "Oczekiwano, że metoda duplicateArrival zwróci true dla duplikatu");
    }

    @Test
    void testIsDuplicateShipsWhileIsNotDuplicate() {

        when(shipRepository.findByShipNameAndArrivalDateAndArrivalTime(ship.getShipName(), ship.getArrivalDate(), ship.getArrivalTime()))
                .thenReturn(Collections.singletonList(ship));

        Ship newShip = ship;
        newShip.setShipName("new");
        newShip.setArrivalDate(LocalDate.now());
        newShip.setArrivalTime(LocalTime.now());

        boolean isDuplicate = shipService.duplicateArrival(newShip);

        Assertions.assertFalse(isDuplicate, "Oczekiwano, że metoda duplicateArrival zwróci false dla duplikatu");
    }

    @Test
    void testIsDuplicateShipsForEditWhileIsNotDuplicate() {

        when(shipRepository.findByShipNameAndArrivalDateAndArrivalTime(ship.getShipName(), ship.getArrivalDate(), ship.getArrivalTime()))
                .thenReturn(Collections.singletonList(ship));

        boolean isDuplicate = shipService.duplicateArrivalEdit(ship);

        Assertions.assertFalse(isDuplicate, "Oczekiwano, że metoda duplicateArrival false dla duplikatu");
    }

    @Test
    void testIsDuplicateShipsForEditWhileIsDuplicate() {

        when(shipRepository.findByShipNameAndArrivalDateAndArrivalTime(ship.getShipName(), ship.getArrivalDate(), ship.getArrivalTime()))
                .thenReturn(Collections.singletonList(ship));

        Ship duplicate = ship;
        duplicate.setId(2L);
        boolean isDuplicate = shipService.duplicateArrivalEdit(duplicate);

        Assertions.assertFalse(isDuplicate, "Oczekiwano, że metoda duplicateArrival true dla duplikatu");
    }

}

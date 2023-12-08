package com.example.ships.integration;

import com.example.ships.model.Role;
import com.example.ships.model.User;
import com.example.ships.repo.UserRepository;
import com.example.ships.service.UserService;
import com.example.ships.util.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Test
    void testRegisterAndLogin() {
        User user = new User("TestUser2", "john2@example.com", "P@ssw0rd2");
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);

        userService.saveUser(user, roles);

        User loginUser = new User("TestUser2","john2@example.com", "P@ssw0rd2");

        Assertions.assertTrue(userService.isValidUser(loginUser));

        User loggedInUser = userService.getUserByEmail(loginUser.getEmail());
        Assertions.assertNotNull(loggedInUser);

        String token = jwtUtil.generateToken(loggedInUser.getEmail());

        Assertions.assertNotNull(token);

        Boolean isAdmin = userService.isAdminRole(loggedInUser.getRoles());
        Assertions.assertFalse(isAdmin);
    }

    @Test
    void testAdminLogin() {
        User adminUser = new User("AdminUser", "admin2@example.com", "AdminP@ssw0rd");
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(Role.ADMIN);

        userService.saveUser(adminUser, adminRoles);


        User loginAdmin = new User("AdminUser", "admin2@example.com", "AdminP@ssw0rd");

        Assertions.assertTrue(userService.isValidUser(loginAdmin));

        User loggedInAdmin = userService.getUserByEmail(loginAdmin.getEmail());
        Assertions.assertNotNull(loggedInAdmin);

        String token = jwtUtil.generateToken(loggedInAdmin.getEmail());

        Assertions.assertNotNull(token);

        Boolean isAdmin = userService.isAdminRole(loggedInAdmin.getRoles());
        Assertions.assertTrue(isAdmin);
    }
}
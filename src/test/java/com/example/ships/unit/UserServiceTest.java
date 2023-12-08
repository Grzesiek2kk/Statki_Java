package com.example.ships.unit;


import com.example.ships.model.Role;
import com.example.ships.model.User;
import com.example.ships.service.UserService;
import com.example.ships.repo.UserRepository;
import com.example.ships.util.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

	private UserRepository userRepository;
	private BCryptPasswordEncoder passwordEncoder;
	private JwtUtil jwtUtil;
	private UserService userService;

	@BeforeEach
	void setUp() {
		userRepository = mock(UserRepository.class);
		passwordEncoder = mock(BCryptPasswordEncoder.class);
		jwtUtil = mock(JwtUtil.class);
		userService = new UserService(userRepository, passwordEncoder, jwtUtil);
	}

	@Test
	void testIsAdminRoleWhenRoleContainsAdmin() {
		UserService user = new UserService(userRepository, passwordEncoder, jwtUtil);
		Set<Role> roles = new HashSet<>();
		roles.add(Role.ADMIN);


		boolean result = user.isAdminRole(roles);

		assertTrue(result);
	}

	@Test
	void testIsAdminRoleWhenRoleDoesNotContainAdmin() {
		Set<Role> roles = new HashSet<>();
		roles.add(Role.USER);

		boolean result = userService.isAdminRole(roles);

		Assertions.assertFalse(result);
	}

	@Test
	void testIsUserEmailUniqueWhenEmailIsUnique() {
		String email = "john@example.com";
		when(userRepository.findByEmail(email)).thenReturn(null);

		boolean isUnique = userService.isUserEmailUnique(email);

		assertTrue(isUnique);
	}

	@Test
	void testIsUserEmailUniqueWhenEmailIsNotUnique() {
		String email = "admin@example.com";
		User existingUser = new User();
		when(userRepository.findByEmail(email)).thenReturn(existingUser);

		boolean isUnique = userService.isUserEmailUnique(email);

		assertFalse(isUnique);
	}

	@Test
	void testIsPasswordValidWithValidPassword() {
		String validPassword = "StrongP@ssword1";

		boolean result = userService.isPasswordValid(validPassword);

		Assertions.assertTrue(result);
	}

	@Test
	void testIsPasswordValidWithInvalidPassword() {

		String invalidPassword = "weakpassword";

		boolean result = userService.isPasswordValid(invalidPassword);

		Assertions.assertFalse(result);
	}

	@Test
	void testSaveUser() {
		User user = new User("TestUser", "john@example.com", "P@ssw0rd");
		Set<Role> roles = new HashSet<>();
		roles.add(Role.USER);

		when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
			User savedUser = invocation.getArgument(0);
			savedUser.setId(1L);
			return savedUser;
		});

		userService.saveUser(user, roles);

		User savedUser = userRepository.findByEmail("john@example.com");
		verify(userRepository).save(argThat(userArgument -> userArgument.getEmail().equals("john@example.com")));

		Assertions.assertNotNull(user.getId(), "Użytkownik powinien mieć przypisane ID po zapisaniu");
	}

	@Test
	void testIsValidUserWhenUserExistsAndPasswordsMatch() {
		String userEmail = "john@example.com";
		String userPassword = "P@ssw0rd";
		User existingUser = new User("TestUser", userEmail, userPassword);

		when(userRepository.findByEmail(userEmail)).thenReturn(existingUser);
		when(passwordEncoder.matches(userPassword, existingUser.getPassword())).thenReturn(true);

		User userToValidate = new User("TestUser", userEmail, userPassword);
		boolean isValid = userService.isValidUser(userToValidate);

		Assertions.assertTrue(isValid);
	}

	@Test
	void testIsValidUserWhenUserDoesNotExist() {
		String userEmail = "john@example.com";
		String userPassword = "P@ssw0rd";

		when(userRepository.findByEmail(userEmail)).thenReturn(null);

		User userToValidate = new User("TestUser", userEmail, userPassword);
		boolean isValid = userService.isValidUser(userToValidate);

		Assertions.assertFalse(isValid);
	}

	@Test
	void testIsValidUserWhenPasswordsDoNotMatch() {
		String userEmail = "john@example.com";
		String userPassword = "P@ssw0rd";
		User existingUser = new User("TestUser", userEmail, userPassword);

		when(userRepository.findByEmail(userEmail)).thenReturn(existingUser);
		when(passwordEncoder.matches(userPassword, existingUser.getPassword())).thenReturn(false);

		User userToValidate = new User("TestUser", userEmail, userPassword);
		boolean isValid = userService.isValidUser(userToValidate);

		Assertions.assertFalse(isValid);
	}
}

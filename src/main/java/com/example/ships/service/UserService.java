package com.example.ships.service;

import com.example.ships.model.User;
import com.example.ships.model.Role;
import com.example.ships.repo.UserRepository;
import com.example.ships.util.JwtUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Set;

@Service
public class UserService implements UserDetailsService{
    @Value("${admin.emails}")
    private String adminEmails;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public boolean isUserEmailUnique(String email) {
        return userRepository.findByEmail(email) == null;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void saveUser(User user, Set<Role> roles) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
        userRepository.save(user);
    }

    public boolean isValidUser(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        return existingUser != null && passwordEncoder.matches(user.getPassword(), existingUser.getPassword());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    public boolean isAdmin(String email) {
        String[] adminEmailArray = adminEmails.split(",");
        for (String adminEmail : adminEmailArray) {
            if (email.equals(adminEmail.trim())) {
                return true;
            }
        }
        return false;
    }
}

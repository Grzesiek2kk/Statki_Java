package com.example.statki.service;

import com.example.statki.model.User;
import com.example.statki.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public boolean isUserEmailUnique(String email) {
        return userRepository.findByEmail(email) == null;
    }

}
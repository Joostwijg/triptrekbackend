package com.triptrek.triptrek.service;

import com.triptrek.triptrek.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.triptrek.triptrek.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtService jwtService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User registerUser(User user){
        String email = user.getEmail().toLowerCase();
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email address already in use.");
        }

        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findUserByEmail(String email){
        return userRepository.findByEmail(email.toLowerCase());
    }

    public User updateUser(User user){
        return userRepository.save(user);
    }

    public User findUserByToken(String token) {
        String email = jwtService.extractEmail(token);
        if (email == null) {
            throw new RuntimeException("Invalid token");
        }
        return userRepository.findByEmail(email.toLowerCase());
    }
}
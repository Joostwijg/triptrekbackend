package com.triptrek.triptrek.service;

import com.triptrek.triptrek.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.triptrek.triptrek.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    public User registerUser(User user){
        String email = user.getEmail().toLowerCase();
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email address already in use.");
        }

        user.setEmail(email);
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
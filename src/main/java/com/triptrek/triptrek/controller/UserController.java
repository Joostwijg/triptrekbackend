package com.triptrek.triptrek.controller;


import com.triptrek.triptrek.model.User;
import com.triptrek.triptrek.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin (origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public User loginUser(@RequestBody User loginDetails) {
        User user = userService.findUserByEmail(loginDetails.getEmail());
        if (user != null && user.getPassword().equals(loginDetails.getPassword())) {
            return user;
        } else {
            throw new RuntimeException("Invalid email or password");
        }

    }
}

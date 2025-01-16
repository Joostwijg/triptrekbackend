package com.triptrek.triptrek.controller;


import com.triptrek.triptrek.dto.RegisterRequest;
import com.triptrek.triptrek.model.User;
import com.triptrek.triptrek.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin (origins = "http://localhost:5177")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        try {
            // Controleer of het wachtwoord en confirmPassword overeenkomen
            if (!request.getPassword().equals(request.getConfirmPassword())) {
                return ResponseEntity.badRequest().body("Passwords do not match!");
            }

            // Maak een nieuwe gebruiker op basis van de aanvraag
            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());

            // Registreer de gebruiker
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

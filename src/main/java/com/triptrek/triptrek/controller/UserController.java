package com.triptrek.triptrek.controller;

import com.triptrek.triptrek.dto.LoginResponse;
import com.triptrek.triptrek.dto.RegisterRequest;
import com.triptrek.triptrek.model.User;
import com.triptrek.triptrek.service.JwtService;
import com.triptrek.triptrek.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin (origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

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
    public ResponseEntity<LoginResponse>  loginUser(@RequestBody User loginDetails) {
        User user = userService.findUserByEmail(loginDetails.getEmail());

        if (user != null && user.getPassword().equals(loginDetails.getPassword())) {
            String token = jwtService.generateToken(user.getEmail());
            System.out.println(token);

            user.setToken(token);
            userService.updateUser(user);

            LoginResponse loginResponse = new LoginResponse(user, token);
            return ResponseEntity.ok(new LoginResponse(user, token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(null, "Invalid email or password"));
        }

    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
        }

        String token = authHeader.replace("Bearer ", "");
        User user = userService.findUserByToken(token);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found or invalid token");
        }

        return ResponseEntity.ok(user);
    }


}


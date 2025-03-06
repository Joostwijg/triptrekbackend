package com.triptrek.triptrek.controller;


import com.triptrek.triptrek.dto.LoginResponse;
import com.triptrek.triptrek.dto.RegisterRequest;
import com.triptrek.triptrek.model.User;
import com.triptrek.triptrek.service.JwtService;
import com.triptrek.triptrek.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin (origins = "http://localhost:5176")
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

            LoginResponse loginResponse = new LoginResponse(user, token);

            return ResponseEntity.ok(loginResponse);
        } else {
            throw new RuntimeException("Invalid email or password");
        }

    }
}


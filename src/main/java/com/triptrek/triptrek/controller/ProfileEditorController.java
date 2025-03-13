package com.triptrek.triptrek.controller;


import com.triptrek.triptrek.dto.ProfileEditorRequest;
import com.triptrek.triptrek.model.User;
import com.triptrek.triptrek.service.JwtService;
import com.triptrek.triptrek.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users/profile")
@CrossOrigin(origins = "http://localhost:5174")
public class ProfileEditorController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    @PutMapping("/edit")
    public ResponseEntity<?> editProfile (@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader, @RequestBody ProfileEditorRequest request) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String email = jwtService.extractEmail(token);

            User user = userService.findUserByEmail(email);
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setAddress(request.getAddress());
            user.setCity(request.getCity());
            user.setState(request.getState());
            user.setZipCode(request.getZipCode());
            user.setCountry(request.getCountry());

            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                if (!request.getPassword().equals(request.getConfirmPassword())) {
                    return ResponseEntity.badRequest().body("Passwords do not match");
                }
                user.setPassword(request.getPassword());
            }

            userService.updateUser(user);

            return ResponseEntity.ok(user);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

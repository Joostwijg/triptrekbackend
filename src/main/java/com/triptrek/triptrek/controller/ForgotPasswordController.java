package com.triptrek.triptrek.controller;

import com.triptrek.triptrek.dto.ForgotPasswordRequest;
import com.triptrek.triptrek.service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class ForgotPasswordController {

    @Autowired
    private ForgotPasswordService service;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String response = service.forgotPassword(request.getEmail());

        if ("Email not registered".equalsIgnoreCase(response)) {
            return ResponseEntity.badRequest().body(response);
        }
        String resetUrl = "http://localhost:8080/api/users/reset-password?token=" + response;
        return ResponseEntity.ok(resetUrl);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String password) {
        return ResponseEntity.ok(service.resetPassword(token, password));
    }
}
package com.triptrek.triptrek.controller;


import com.triptrek.triptrek.dto.ForgotPasswordRequest;
import com.triptrek.triptrek.service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class ForgotPasswordController {

    @Autowired
    private ForgotPasswordService service;

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String email = request.getEmail();
        String response = service.forgotPassword(email);

        if(!response.startsWith("Invalid")){
            response= "http://localhost:8080/api/users/reset-password?token=" + response;
        }
        return response;
    }

    @PutMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String password){
        return service.resetPassword(token,password);
    }
}


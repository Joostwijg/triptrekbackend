package com.triptrek.triptrek.controller;


import com.triptrek.triptrek.dto.ProfileEditorRequest;
import com.triptrek.triptrek.model.User;
import com.triptrek.triptrek.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users/profile/edit")
@CrossOrigin(origins = "http://localhost:5173")
public class ProfileEditorController {

    @Autowired
    private UserService userService;

    @PutMapping("/edit")
    public ResponseEntity<?> editProfile(@RequestBody ProfileEditorRequest request){
        try {
            User user = userService.findUserByEmail(request.getEmail());
            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
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
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

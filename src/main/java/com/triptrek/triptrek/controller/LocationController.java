package com.triptrek.triptrek.controller;


import com.triptrek.triptrek.model.Location;
import com.triptrek.triptrek.service.LocationService;
import jakarta.annotation.PostConstruct;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "http://localhost:5175")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping
    public List<Location> getAllLocations(){
        return locationService.getApprovedLocations();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Long id){
        Optional<Location> location = locationService.getLocationById(id);
        return location.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<Location> getLocationBySlug(@PathVariable String slug) {
        return locationService.getLocationBySlug(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Location> addLocation(@RequestBody Location location){
        location.setApproved(false);
        return ResponseEntity.ok(locationService.addLocation(location));
    }

    @GetMapping("/pending")
    public List<Location> getPendingLocations(){
        return locationService.getPendingLocations();
    }

    @PatchMapping("/approve/{id}")
    public ResponseEntity<Location> approveLocation(@PathVariable Long id){
        return ResponseEntity.ok(locationService.approveLocation(id));
    }

    @DeleteMapping("/reject/{id}")
    public ResponseEntity<?> rejectLocation(@PathVariable Long id){
        Optional<Location> location = locationService.getLocationById(id);
        if (location.isPresent() && !location.get().isApproved()){
            locationService.deleteLocation(id);
            return ResponseEntity.ok("Location rejected en deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Location not found or already approved");
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            Path uploadDir = Paths.get("uploads");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path filePath = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.ok("http://localhost:8080/uploads/" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }





}

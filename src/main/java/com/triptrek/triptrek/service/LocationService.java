package com.triptrek.triptrek.service;

import com.triptrek.triptrek.model.Location;
import com.triptrek.triptrek.repository.LocationRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }

    public List<Location> getPendingLocations(){
        return locationRepository.findByApprovedFalse();
    }

    public Location approveLocation(Long id){
        Location location = locationRepository.findById(id).orElseThrow();
        location.setApproved(true);
        return locationRepository.save(location);
    }

    public void deleteLocation(Long id){
        locationRepository.deleteById(id);
    }

    public List<Location> getApprovedLocations(){
        return locationRepository.findByApprovedTrue();
    }

    public Location addLocation(Location location) {
        String slug = generateUniqueSlug(location.getName());
        location.setSlug(slug);
        location.setApproved(false);
        return locationRepository.save(location);
    }


    private String generateSlug(String name) {
        return name
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "");
    }

    public Optional<Location> getLocationBySlug(String slug) {
        return locationRepository.findBySlug(slug);
    }

    public String generateUniqueSlug(String baseName) {
        String baseSlug = generateSlug(baseName);
        String slug = baseSlug;
        int counter = 1;

        while (locationRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }

        return slug;
    }

    @PostConstruct
    public void generateSlugsForExistingLocations() {
        List<Location> all = locationRepository.findAll();
        for (Location loc : all) {
            if (loc.getSlug() == null || loc.getSlug().isBlank()) {
                String slug = generateUniqueSlug(loc.getName());
                loc.setSlug(slug);
                locationRepository.save(loc);
            }
        }
    }


}

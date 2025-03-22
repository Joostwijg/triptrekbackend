package com.triptrek.triptrek.service;

import com.triptrek.triptrek.model.Location;
import com.triptrek.triptrek.repository.LocationRepository;
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

    public Location addLocation(Location location) {
        return locationRepository.save(location);
    }
}

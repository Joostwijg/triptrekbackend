package com.triptrek.triptrek.service;

import com.triptrek.triptrek.model.Location;
import com.triptrek.triptrek.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LocationServiceTest {

    private LocationRepository locationRepository;
    private LocationService locationService;

    @BeforeEach
    void setup() {
        locationRepository = mock(LocationRepository.class);
        locationService = new LocationService();
        locationService.locationRepository = locationRepository;
    }

    @Test
    void testAddLocation_generatesUniqueSlug() {
        Location location = new Location();
        location.setName("Test Location");

        when(locationRepository.existsBySlug("testlocation")).thenReturn(false);
        when(locationRepository.save(any(Location.class))).thenAnswer(i -> i.getArguments()[0]);

        Location saved = locationService.addLocation(location);

        assertEquals("testlocation", saved.getSlug());
        assertFalse(saved.isApproved());
    }

    @Test
    void testGenerateUniqueSlug_withDuplicates() {
        when(locationRepository.existsBySlug("test")).thenReturn(true);
        when(locationRepository.existsBySlug("test-1")).thenReturn(true);
        when(locationRepository.existsBySlug("test-2")).thenReturn(false);

        String unique = locationService.generateUniqueSlug("Test");

        assertEquals("test-2", unique);
    }

    @Test
    void testApproveLocation() {
        Location location = new Location();
        location.setApproved(false);

        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(locationRepository.save(any(Location.class))).thenAnswer(i -> i.getArguments()[0]);

        Location approved = locationService.approveLocation(1L);
        assertTrue(approved.isApproved());
    }

    @Test
    void testDeleteLocation() {
        doNothing().when(locationRepository).deleteById(1L);
        locationService.deleteLocation(1L);
        verify(locationRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetLocationById() {
        Location location = new Location();
        when(locationRepository.findById(2L)).thenReturn(Optional.of(location));

        Optional<Location> result = locationService.getLocationById(2L);
        assertTrue(result.isPresent());
    }
}

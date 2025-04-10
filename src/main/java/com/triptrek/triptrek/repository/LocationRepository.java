package com.triptrek.triptrek.repository;

import com.triptrek.triptrek.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByApprovedFalse();
    List<Location> findByApprovedTrue();
    Optional<Location> findBySlug(String slug);
    boolean existsBySlug(String slug);

}

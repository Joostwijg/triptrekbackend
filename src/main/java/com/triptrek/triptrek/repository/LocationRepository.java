package com.triptrek.triptrek.repository;

import com.triptrek.triptrek.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}

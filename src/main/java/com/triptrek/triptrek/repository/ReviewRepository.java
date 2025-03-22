package com.triptrek.triptrek.repository;

import com.triptrek.triptrek.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByLocationId(Long locationId);
}

package com.triptrek.triptrek.controller;

import com.triptrek.triptrek.model.Review;
import com.triptrek.triptrek.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:5174")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/location/{locationId}")
    public List<Review> getReviewsByLocationId(@PathVariable Long locationId) {
        return reviewService.getReviewsByLocationId(locationId);
    }

    @PostMapping
    public ResponseEntity<Review> addReview(@RequestBody Review review) {
        return ResponseEntity.ok(reviewService.addReview(review));
    }

}

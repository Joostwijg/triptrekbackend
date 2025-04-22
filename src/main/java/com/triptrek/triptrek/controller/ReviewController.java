package com.triptrek.triptrek.controller;

import com.triptrek.triptrek.model.Review;
import com.triptrek.triptrek.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/location/id/{locationId}")
    public List<Review> getReviewsByLocationId(@PathVariable Long locationId) {
        return reviewService.getReviewsByLocationId(locationId).stream()
                .filter(Review::isApproved)
                .toList();
    }

    @PostMapping
    public ResponseEntity<Review> addReview(@RequestBody Review review) {
        review.setApproved(false);
        return ResponseEntity.ok(reviewService.addReview(review));
    }

    @GetMapping("/pending")
    public List<Review> getPendingReviews() {
        return reviewService.getPendingReviews();
    }

    @PatchMapping("/approve/{id}")
    public ResponseEntity<Review> approveReview(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.approveReview(id));
    }

    @DeleteMapping("/reject/{id}")
    public ResponseEntity<?> rejectReview(@PathVariable Long id) {
        Optional<Review> review = reviewService.getReviewById(id);
        if (review.isPresent() && !review.get().isApproved()) {
            reviewService.deleteReview(id);
            return ResponseEntity.ok("Review rejected and deleted");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Review not found or already accepted");
        }
    }
}
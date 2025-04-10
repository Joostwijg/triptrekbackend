package com.triptrek.triptrek.service;

import com.triptrek.triptrek.model.Review;
import com.triptrek.triptrek.repository.LocationRepository;
import com.triptrek.triptrek.repository.ReviewRepository;
import com.triptrek.triptrek.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    public List<Review> getReviewsByLocationId(Long locationId) {
        return reviewRepository.findByLocationId(locationId);
    }

    public List<Review> getPendingReviews() {
        return reviewRepository.findByApprovedFalse();
    }

    public Review approveReview(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow();
        review.setApproved(true);
        return reviewRepository.save(review);
    }

    public Review addReview(Review review) {
        Integer userId = review.getUser().getId();
        Long locationId = review.getLocation().getId();

        var user = userRepository.findById(userId).orElseThrow();
        var location = locationRepository.findById(locationId).orElseThrow();

        review.setUser(user);
        review.setLocation(location);

        return reviewRepository.save(review);
    }

    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}

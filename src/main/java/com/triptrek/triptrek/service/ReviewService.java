package com.triptrek.triptrek.service;

import com.triptrek.triptrek.model.Review;
import com.triptrek.triptrek.repository.LocationRepository;
import com.triptrek.triptrek.repository.ReviewRepository;
import com.triptrek.triptrek.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Review addReview(Review review) {
        Integer userId = review.getUser().getId();
        Long locationId = review.getLocation().getId();

        var user = userRepository.findById(userId).orElseThrow();
        var location = locationRepository.findById(locationId).orElseThrow();

        review.setUser(user);
        review.setLocation(location);

        return reviewRepository.save(review);
    }
}

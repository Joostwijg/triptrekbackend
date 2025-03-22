package com.triptrek.triptrek.service;


import com.triptrek.triptrek.model.Review;
import com.triptrek.triptrek.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> getReviewsByLocationId(Long locationId) {
        return reviewRepository.findByLocationId(locationId);
    }

    public Review addReview (Review review) {
        return reviewRepository.save(review);
    }

}

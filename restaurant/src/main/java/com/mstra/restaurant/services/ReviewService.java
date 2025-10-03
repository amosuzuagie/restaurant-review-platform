package com.mstra.restaurant.services;

import com.mstra.restaurant.domain.ReviewCreateUpdateRequest;
import com.mstra.restaurant.domain.entities.Review;
import com.mstra.restaurant.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReviewService {
    Review createReview(User author, String restaurantId, ReviewCreateUpdateRequest review);
    Page<Review> listReviews(String restaurantId, Pageable pageable);
    Optional<Review> getReview(String restaurantId, String reviewId);
    Review updateReview(User author, String restaurantId, String reviewId, ReviewCreateUpdateRequest request);
    void deleteReview(User user, String restaurantId, String reviewId);
}

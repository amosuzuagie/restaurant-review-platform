package com.mstra.restaurant.services;

import com.mstra.restaurant.domain.ReviewCreateUpdateRequest;
import com.mstra.restaurant.domain.entities.Review;
import com.mstra.restaurant.domain.entities.User;

public interface ReviewService {
    Review createReview(User author, String restaurantId, ReviewCreateUpdateRequest review);
}

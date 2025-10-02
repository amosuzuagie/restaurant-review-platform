package com.mstra.restaurant.services.impl;

import com.mstra.restaurant.domain.ReviewCreateUpdateRequest;
import com.mstra.restaurant.domain.entities.Photo;
import com.mstra.restaurant.domain.entities.Restaurant;
import com.mstra.restaurant.domain.entities.Review;
import com.mstra.restaurant.domain.entities.User;
import com.mstra.restaurant.exceptins.RestaurantNotFoundException;
import com.mstra.restaurant.exceptins.ReviewNotAllowedException;
import com.mstra.restaurant.repositories.RestaurantRepository;
import com.mstra.restaurant.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final RestaurantRepository restaurantRepository;

    @Override
    public Review createReview(User author, String restaurantId, ReviewCreateUpdateRequest review) {
        Restaurant restaurant = getRestaurant(restaurantId);

        boolean hasExistingReview = restaurant.getReviews().stream().anyMatch(
                r -> r.getWrittenBy().getId().equals(author.getId())
        );

        if (hasExistingReview)
            throw new ReviewNotAllowedException("User has already reviewed this restaurant");

        LocalDateTime now = LocalDateTime.now();

        List<Photo> photos = review.getPhotoIds().stream().map(url -> {
            return Photo.builder()
                    .url(url)
                    .uploadDate(now)
                    .build();
        }).toList();

        String reviewId = UUID.randomUUID().toString();

        Review reviewToCreate = Review.builder()
                .id(reviewId)
                .content(review.getContent())
                .rating(review.getRating())
                .photos(photos)
                .datePosted(now)
                .lastEdited(now)
                .writtenBy(author)
                .build();

        restaurant.getReviews().add(reviewToCreate);

        updateRestaurantAverageReviews(restaurant);

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return savedRestaurant.getReviews().stream()
                .filter(r-> r.getId().equals(reviewId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Error retrieving created review."));
    }

    private Restaurant getRestaurant(String restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(
                        () -> new RestaurantNotFoundException(String.format("Restaurant with ID: %s not fount.", restaurantId))
                );
    }

    private void updateRestaurantAverageReviews(Restaurant restaurant) {
        List<Review> reviews = restaurant.getReviews();

        if (reviews.isEmpty()) {
            restaurant.setAverageRating(0.0f);
        } else {
            double averageRating = reviews.stream()
                    .mapToDouble(Review::getRating)
                    .average()
                    .orElse(0.0);
            restaurant.setAverageRating((float) averageRating);
        }
    }
}

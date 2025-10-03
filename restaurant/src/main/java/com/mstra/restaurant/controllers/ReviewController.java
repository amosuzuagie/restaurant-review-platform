package com.mstra.restaurant.controllers;

import com.mstra.restaurant.domain.ReviewCreateUpdateRequest;
import com.mstra.restaurant.domain.dtos.ReviewCreateUpdateRequestDto;
import com.mstra.restaurant.domain.dtos.ReviewDto;
import com.mstra.restaurant.domain.entities.Review;
import com.mstra.restaurant.domain.entities.User;
import com.mstra.restaurant.mappers.ReviewMapper;
import com.mstra.restaurant.services.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurant/{restaurantId}/reviews")
public class ReviewController {
    private final ReviewMapper reviewMapper;
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(
            @PathVariable String restaurantId,
            @Valid @RequestBody ReviewCreateUpdateRequestDto review,
            @AuthenticationPrincipal Jwt jwt
    ){
        ReviewCreateUpdateRequest request = reviewMapper.toReviewCreateUpdateRequest(review);

        User user = extractUser(jwt);

        Review createdReview = reviewService.createReview(user, restaurantId, request);

        return ResponseEntity.ok(reviewMapper.toReviewDto(createdReview));
    }

    @GetMapping
    public Page<ReviewDto> listReviews(
            @PathVariable String restaurantId,
            @PageableDefault(
                    size = 20,
                    page = 0,
                    sort = "datePosted",
                    direction = Sort.Direction.DESC
            )Pageable pageable
    ){
        return reviewService
                .listReviews(restaurantId, pageable)
                .map(reviewMapper::toReviewDto);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> getReview(@PathVariable String restaurantId, @PathVariable String reviewId) {
        return reviewService.getReview(restaurantId, reviewId)
                .map(reviewMapper::toReviewDto)
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(
            @PathVariable String restaurantId, @PathVariable String reviewId,
            @Valid @RequestBody ReviewCreateUpdateRequestDto review, @AuthenticationPrincipal Jwt jwt
    ) {
        ReviewCreateUpdateRequest request = reviewMapper.toReviewCreateUpdateRequest(review);
        User author = extractUser(jwt);

        Review updateReview = reviewService.updateReview(author, restaurantId, reviewId, request);

        return ResponseEntity.ok(reviewMapper.toReviewDto(updateReview));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable String restaurantId, @PathVariable String reviewId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        User user = extractUser(jwt);
        reviewService.deleteReview(user, restaurantId, reviewId);
        return ResponseEntity.noContent().build();
    }

    private User extractUser(Jwt jwt) {
        return User.builder()
                .id(jwt.getSubject())
                .username(jwt.getClaimAsString("preferred_username"))
                .givenName(jwt.getClaimAsString("given_name"))
                .familyName(jwt.getClaimAsString("family_name"))
                .build();
    }
}

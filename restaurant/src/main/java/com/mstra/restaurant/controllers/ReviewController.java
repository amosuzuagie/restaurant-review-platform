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

    private User extractUser(Jwt jwt) {
        return User.builder()
                .id(jwt.getSubject())
                .username(jwt.getClaimAsString("preferred_username"))
                .givenName(jwt.getClaimAsString("given_name"))
                .familyName(jwt.getClaimAsString("family_name"))
                .build();
    }
}

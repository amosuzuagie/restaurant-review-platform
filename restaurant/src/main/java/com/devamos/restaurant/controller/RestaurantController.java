package com.devamos.restaurant.controller;

import com.devamos.restaurant.domain.RestaurantCreateUpdateRequest;
import com.devamos.restaurant.domain.dtos.RestaurantCreateUpdateRequestDto;
import com.devamos.restaurant.domain.dtos.RestaurantDto;
import com.devamos.restaurant.domain.dtos.RestaurantSummaryDto;
import com.devamos.restaurant.domain.entities.Restaurant;
import com.devamos.restaurant.mappers.RestaurantMapper;
import com.devamos.restaurant.services.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;

    @PostMapping
    public ResponseEntity<RestaurantDto> createRestaurant(
            @Valid @RequestBody RestaurantCreateUpdateRequestDto request
    ) {
        RestaurantCreateUpdateRequest createUpdateRequest = restaurantMapper.toRestaurantCreateUpdateRequest(request);
        Restaurant restaurant = restaurantService.createRestaurant(createUpdateRequest);
        return ResponseEntity.ok(restaurantMapper.toRestaurantDto(restaurant));
    }

    @GetMapping
    public Page<RestaurantSummaryDto> searchRestaurant(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Float minRating,
            @RequestParam(required = false) Float latitude,
            @RequestParam(required = false) Float longitude,
            @RequestParam(required = false) Float radius,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<Restaurant> searchResult = restaurantService.searchRestaurants(
                q, minRating, latitude, longitude, radius, PageRequest.of(page - 1, size)
        );

        return searchResult.map(restaurantMapper::toRestaurantSummaryDto);
    }

    @GetMapping("/{restaurant_id}")
    public ResponseEntity<RestaurantDto> getRestaurant(@PathVariable("restaurant_id")String restaurantId) {
        return restaurantService.getRestaurant(restaurantId)
                .map(restaurant -> ResponseEntity.ok(restaurantMapper.toRestaurantDto(restaurant)))
                .orElse(ResponseEntity.notFound().build());
    }
}

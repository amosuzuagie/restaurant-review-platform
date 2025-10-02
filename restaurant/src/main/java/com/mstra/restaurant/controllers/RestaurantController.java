package com.mstra.restaurant.controllers;

import com.mstra.restaurant.domain.RestaurantCreateUpdateRequest;
import com.mstra.restaurant.domain.dtos.RestaurantCreateUpdateRequestDto;
import com.mstra.restaurant.domain.dtos.RestaurantDto;
import com.mstra.restaurant.domain.entities.Restaurant;
import com.mstra.restaurant.mappers.RestaurantMapper;
import com.mstra.restaurant.services.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
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
}

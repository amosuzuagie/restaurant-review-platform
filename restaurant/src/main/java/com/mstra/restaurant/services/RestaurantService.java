package com.mstra.restaurant.services;

import com.mstra.restaurant.domain.RestaurantCreateUpdateRequest;
import com.mstra.restaurant.domain.entities.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantService {
    Restaurant createRestaurant(RestaurantCreateUpdateRequest request);
    Page<Restaurant> searchRestaurant(
            String query, Float minRating, Float latitude, Float longitude, Float radius, Pageable pageable
    );
}

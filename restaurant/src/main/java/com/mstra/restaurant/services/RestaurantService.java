package com.mstra.restaurant.services;

import com.mstra.restaurant.domain.RestaurantCreateUpdateRequest;
import com.mstra.restaurant.domain.entities.Restaurant;

public interface RestaurantService {
    Restaurant createRestaurant(RestaurantCreateUpdateRequest request);
}

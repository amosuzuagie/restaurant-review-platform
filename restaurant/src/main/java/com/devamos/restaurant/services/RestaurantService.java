package com.devamos.restaurant.services;

import com.devamos.restaurant.domain.RestaurantCreateUpdateRequest;
import com.devamos.restaurant.domain.entities.Restaurant;

public interface RestaurantService {
    Restaurant createRestaurant(RestaurantCreateUpdateRequest request);
}

package com.devamos.restaurant.services;

import com.devamos.restaurant.domain.GeoLocation;
import com.devamos.restaurant.domain.entities.Address;

public interface GeoLocationService {
    GeoLocation geolocation(Address address);
}

package com.mstra.restaurant.services;

import com.mstra.restaurant.domain.GeoLocation;
import com.mstra.restaurant.domain.entities.Address;

public interface GeoLocationService {
    GeoLocation geoLocate(Address address);
}

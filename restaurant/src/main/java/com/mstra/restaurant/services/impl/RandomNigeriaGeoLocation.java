package com.mstra.restaurant.services.impl;

import com.mstra.restaurant.domain.GeoLocation;
import com.mstra.restaurant.domain.entities.Address;
import com.mstra.restaurant.services.GeoLocationService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomNigeriaGeoLocation  implements GeoLocationService {
    /**
     * RandomLondonGeoLocationService for test data
     * The service randomly generates locations and
     * uses a bounding box to ensure coordinates fall
     * within Nigeria boundaries.
     **/
    private static final float MIN_LATITUDE  = 4.241f;
    private static final float MAX_LATITUDE  = 13.866f;
    private static final float MIN_LONGITUDE = 2.692f;
    private static final float MAX_LONGITUDE = 14.577f;


    @Override
    public GeoLocation geoLocate(Address address) {
        Random random = new Random();
        double latitude = MIN_LATITUDE + random.nextDouble() * (MAX_LATITUDE - MIN_LATITUDE);
        double longitude =  MIN_LONGITUDE + random.nextDouble() * (MAX_LONGITUDE - MIN_LONGITUDE);

        return GeoLocation.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}

package com.devamos.restaurant.services.impl;

import com.devamos.restaurant.domain.GeoLocation;
import com.devamos.restaurant.domain.RestaurantCreateUpdateRequest;
import com.devamos.restaurant.domain.entities.Address;
import com.devamos.restaurant.domain.entities.Photo;
import com.devamos.restaurant.domain.entities.Restaurant;
import com.devamos.restaurant.repositories.RestaurantRepository;
import com.devamos.restaurant.services.GeoLocationService;
import com.devamos.restaurant.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final GeoLocationService geoLocationService;

    @Override
    public Restaurant createRestaurant(RestaurantCreateUpdateRequest request) {
        Address address = request.getAddress();
        GeoLocation geoLocation = geoLocationService.geolocation(address);
        GeoPoint geoPoint = new GeoPoint(geoLocation.getLatitude(), geoLocation.getLongitude());

        List<String> photoIds = request.getPhotoIds();
        List<Photo> photos = photoIds.stream().map(url ->
                Photo.builder()
                        .url(url)
                        .uploadDate(LocalDateTime.now())
                        .build()).toList();

        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .cuisineType(request.getCuisineType())
                .contactInformation(request.getContactInformation())
                .address(address)
                .geoLocation(geoPoint)
                .operatingHours(request.getOperatingHours())
                .averageRating(0f)
                .photos(photos)
                .build();

        return restaurantRepository.save(restaurant);
    }

    @Override
    public Page<Restaurant> searchRestaurants(
            String query, Float minRating, Float latitude, Float longitude, Float radius, Pageable pageable
    ) {
        if (null != minRating && (null == query || query.isEmpty())) {
            System.out.println("ONE: just filtering my min rating");
            return restaurantRepository.findByAverageRatingGreaterThanEqual(minRating, pageable);
        }


        // Make minRating zero if no minRating.
        Float searchMinRating = null == minRating ? 0f : minRating;

        // Check to ensure no empty query and white space query
        if (null != query && !query.trim().isEmpty()) {
            System.out.println("TWO:there's a text, search query");
            return restaurantRepository.findByQueryAndMinRating(query, searchMinRating, pageable);
        }

        if (null != latitude && null != longitude && null != radius) {
            System.out.println("THREE: there's a location search");
            return restaurantRepository.findByLocationNear(latitude, longitude, radius, pageable);
        }

        return restaurantRepository.findAll(pageable);
    }

    @Override
    public Optional<Restaurant> getRestaurant(String id) {
        return restaurantRepository.findById(id);
    }
}

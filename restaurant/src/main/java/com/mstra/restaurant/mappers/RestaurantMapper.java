package com.mstra.restaurant.mappers;

import com.mstra.restaurant.domain.RestaurantCreateUpdateRequest;
import com.mstra.restaurant.domain.dtos.GeoPointDto;
import com.mstra.restaurant.domain.dtos.RestaurantCreateUpdateRequestDto;
import com.mstra.restaurant.domain.dtos.RestaurantDto;
import com.mstra.restaurant.domain.dtos.RestaurantSummaryDto;
import com.mstra.restaurant.domain.entities.Restaurant;
import com.mstra.restaurant.domain.entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantMapper {
    RestaurantCreateUpdateRequest toRestaurantCreateUpdateRequest(RestaurantCreateUpdateRequestDto dto);

    RestaurantDto toRestaurantDto(Restaurant restaurant);

    @Mapping(target = "latitude", expression = "java(geoPoint.getLat())")
    @Mapping(target = "longitude", expression = "java(geoPoint.getLon())")
    GeoPointDto toGeoPointDto(GeoPoint geoPoint);

    @Mapping(source = "reviews", target = "totalReviews", qualifiedByName = "populateReviews")
    RestaurantSummaryDto toRestaurantSummaryDto(Restaurant restaurant);

    @Named("populateReviews")
    default Integer populateReviews(List<Review> reviews) {
        return reviews.size();
    }

}

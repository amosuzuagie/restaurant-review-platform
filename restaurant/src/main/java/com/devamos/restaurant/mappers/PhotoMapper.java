package com.devamos.restaurant.mappers;

import com.devamos.restaurant.domain.dtos.PhotoDto;
import com.devamos.restaurant.domain.entities.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface PhotoMapper {
    PhotoDto toPhotoDto(Photo photo);
}

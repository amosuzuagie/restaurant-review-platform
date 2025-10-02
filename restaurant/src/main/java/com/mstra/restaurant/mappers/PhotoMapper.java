package com.mstra.restaurant.mappers;

import com.mstra.restaurant.domain.dtos.PhotoDto;
import com.mstra.restaurant.domain.entities.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhotoMapper {
    PhotoDto toPhotoDto(Photo photo);
}

package com.mstra.restaurant.services.impl;

import com.mstra.restaurant.domain.entities.Photo;
import com.mstra.restaurant.services.PhotoService;
import com.mstra.restaurant.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Service implementation for handling photo operations.
 * <p>
 * Responsibilities:
 * - Uploads photos by delegating file storage to {@link StorageService}.
 *   Each uploaded photo is assigned a unique ID (UUID) to prevent filename collisions.
 * - Returns a {@link Photo} object containing the file URL and upload timestamp.
 * - Retrieves stored photos as {@link org.springframework.core.io.Resource} for access or download.
 */

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final StorageService storageService;

    @Override
    public Photo uploadPhoto(MultipartFile file) {
        String photoId = UUID.randomUUID().toString();
        String url = storageService.store(file, photoId);

        return Photo.builder()
                .url(url)
                .uploadDate(LocalDateTime.now())
                .build();
    }

    @Override
    public Optional<Resource> getPhotoAsResource(String id) {
        return storageService.loadResource(id);
    }
}

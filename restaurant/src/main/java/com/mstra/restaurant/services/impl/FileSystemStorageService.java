package com.mstra.restaurant.services.impl;

import com.mstra.restaurant.exceptins.StorageException;
import com.mstra.restaurant.services.StorageService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

/**
 * Service for managing file storage on the local file system.
 * <p>
 * Responsibilities:
 * - Initializes the storage directory on application startup.
 * - Stores files safely under a configured root directory.
 * - Prevents path traversal by disallowing storage outside the root location.
 * - Loads files as {@link org.springframework.core.io.Resource} when requested.
 * <p>
 * Notes:
 * - Currently overwrites existing files with the same name.
 * - TODO: Add validation (e.g., malware scanning, file size/type checks, access permissions).
 */

@Slf4j
@Service
public class FileSystemStorageService implements StorageService {
    @Value("${app.storage.location:uploads}")
    private String storageLocation;

    private Path rootLocation;

    @PostConstruct
    public void init() {
        rootLocation = Paths.get(storageLocation);
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException ex) {
            throw new StorageException("Could not initialize storage location", ex);
        }
    }

    @Override
    public String store(MultipartFile file, String filename) {
        try {
            if (file.isEmpty()) throw new StorageException("Cannot save an empty file");

            String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String finalFileName = filename + "." + extension;

            Path destinationFile = rootLocation.resolve(Paths.get(finalFileName))
                    .normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath()))
                throw new StorageException("Cannot store file outside specified directory");

            try(InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            return finalFileName;
        }
        catch (IOException ex) {
            throw new StorageException("Failed to store file", ex);
        }
    }

    @Override
    public Optional<Resource> loadResource(String filename) {
        try {
            Path file = rootLocation.resolve(filename);

            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return Optional.of(resource);
            } else {
                return Optional.empty();
            }
        } catch (MalformedURLException ex) {
            log.warn("Could not read file: {}", filename, ex);
            return Optional.empty();
        }
    }
}

package com.example.system1.Service;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class ImageStorageService {

    private final Path rootLocation = Paths.get("uploads");
    private Cloudinary cloudinary;

    public ImageStorageService(@Value("${CLOUDINARY_URL:}") String cloudinaryUrl) {
        if (cloudinaryUrl != null && !cloudinaryUrl.isEmpty()) {
            cloudinary = new Cloudinary(cloudinaryUrl);
        } else {
            // Check system environment if property is missing
            String envUrl = System.getenv("CLOUDINARY_URL");
            if (envUrl != null && !envUrl.isEmpty()) {
                cloudinary = new Cloudinary(envUrl);
            }
        }
    }

    public String saveImage(MultipartFile file) {
        try {
            if (file.isEmpty()) return "default.png";

            // If Cloudinary is configured, upload to cloud
            if (cloudinary != null) {
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(), 
                    ObjectUtils.asMap("transformation", new com.cloudinary.Transformation().width(800).height(800).crop("limit").quality("auto")));
                return uploadResult.get("secure_url").toString();
            }
            
            // Otherwise, fallback to local storage
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }
            String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), this.rootLocation.resolve(uniqueFilename));
            return uniqueFilename;
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to store image.", e);
        }
    }

    public String saveImageBytes(byte[] fileBytes, String originalFilename) {
        try {
            if (fileBytes == null || fileBytes.length == 0) return "default.png";

            // If Cloudinary is configured, upload to cloud
            if (cloudinary != null) {
                Map uploadResult = cloudinary.uploader().upload(fileBytes, 
                    ObjectUtils.asMap("transformation", new com.cloudinary.Transformation().width(800).height(800).crop("limit").quality("auto")));
                return uploadResult.get("secure_url").toString();
            }
            
            // Otherwise, fallback to local storage
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }
            String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;
            Files.write(this.rootLocation.resolve(uniqueFilename), fileBytes);
            return uniqueFilename;
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to store image.", e);
        }
    }
}
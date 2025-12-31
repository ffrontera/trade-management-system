package com.ffrontera._generales.backend.common.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ffrontera._generales.backend.common.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryStorageServiceImpl implements StorageService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty())
            throw new IOException("File is empty");

        String originalFilename = file.getOriginalFilename();
        String cleanFileName = StringUtils.cleanPath(originalFilename != null ? originalFilename : "unknown");
        String fileNameWhitoutExt = cleanFileName.contains(".") ?
                cleanFileName.substring(0, cleanFileName.lastIndexOf('.')) : cleanFileName;

        String publicId = UUID.randomUUID().toString() + "_" + fileNameWhitoutExt;

        log.info("Uploading file to Cloudinary with public ID: {}", publicId);

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "public_id", publicId,
                        "folder", "ecommerce_products"
                ));
        return uploadResult.get("secure_url").toString();
    }
}

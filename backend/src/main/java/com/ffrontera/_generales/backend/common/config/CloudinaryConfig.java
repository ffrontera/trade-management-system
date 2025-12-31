package com.ffrontera._generales.backend.common.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.cloud_name}")
    private String cloudName;

    @Value("${cloudinary.api_key}")
    private String apiKey;

    @Value("${cloudinary.api_secret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> confing = new HashMap<>();
        confing.put("cloud_name", cloudName);
        confing.put("api_key", apiKey);
        confing.put("api_secret", apiSecret);
        confing.put("secure", "true");
        return new Cloudinary(confing);
    }
}

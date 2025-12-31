package com.ffrontera._generales.backend.common.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "cloudinary")
public class CloudinaryConfig {

    private String cloudName;
    private String apiKey;
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

package com.sdtvnews.sdtvnews.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-ads}")
    String UPLOAD_DIRECTORY_ADS;

    @Value("${file.upload-image}")
    String IMAGE_DIRECTORY;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve images from D:/ads folder at /ads/**
        registry.addResourceHandler("/ads-image/**")
                .addResourceLocations("file:"+UPLOAD_DIRECTORY_ADS);

        // Serve images from D:/uploads folder at /uploads/**
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:"+IMAGE_DIRECTORY);
    }

}

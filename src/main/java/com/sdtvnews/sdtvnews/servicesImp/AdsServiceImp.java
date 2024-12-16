package com.sdtvnews.sdtvnews.servicesImp;

import com.sdtvnews.sdtvnews.config.CustomException;
import com.sdtvnews.sdtvnews.config.GetUserAccess;
import com.sdtvnews.sdtvnews.dto.request.AdsRequest;
import com.sdtvnews.sdtvnews.dto.response.AdsResponse;
import com.sdtvnews.sdtvnews.entity.Ads;
import com.sdtvnews.sdtvnews.repository.AdsRepository;
import com.sdtvnews.sdtvnews.services.AdsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdsServiceImp implements AdsService {

    private final AdsRepository advertiseWithUsRepository;

    @Value("${file.upload-ads}")
    String UPLOAD_DIRECTORY_ADS;

    @Autowired
    GetUserAccess getUserAccess;

    public AdsRequest createAds(AdsRequest request) throws IOException {
        MultipartFile imageFile = request.getImage();

        if (imageFile.isEmpty()) {
            throw new IOException("File is empty");
        }
        // Get the original file name and sanitize it
        String originalFileName = imageFile.getOriginalFilename();
        String sanitizedFileName = originalFileName != null ? originalFileName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_") : "default.jpg";

        // Generate a unique file name to avoid conflicts
        String uniqueFileName = UUID.randomUUID().toString() + "_" + sanitizedFileName;

        // Define the full path where the file will be stored
        String imagePath = UPLOAD_DIRECTORY_ADS + uniqueFileName;

        // Save the file to the server
        File destinationFile = new File(imagePath);
        imageFile.transferTo(destinationFile);

        // Save the metadata to the database (file path)
        Ads ad = new Ads();
        ad.setLocal(request.getLocal());
        ad.setStatus("1");
        ad.setUrl(request.getUrl());
        ad.setImage(uniqueFileName); // Store relative path
        ad.setCreateBy(String.valueOf(getUserAccess.getUserAccess().getId()));
        ad.setCreateDate(LocalDateTime.now());
        advertiseWithUsRepository.save(ad);
        // Convert DTO to entity

        //advertiseWithUsRepository.save(ad);

        // Save the entity to the database
        return request;
    }

    @Override
    public List<AdsResponse> getAllAds() {
        List<Ads> adsList = advertiseWithUsRepository.findAll(); // Retrieve all advertiseWithUs from the repository
        List<AdsResponse> advertiseWithUsResponses = new ArrayList<>();
        for (Ads advertiseWithUs : adsList) {
            AdsResponse response = new AdsResponse();
            response.setId(advertiseWithUs.getId());
            response.setUrl(advertiseWithUs.getUrl());
            response.setImage(advertiseWithUs.getImage());
            response.setStatus(advertiseWithUs.getStatus());
            response.setCreateDate(advertiseWithUs.getCreateDate());
            response.setLocal(advertiseWithUs.getLocal());
            // Set other necessary fields
            advertiseWithUsResponses.add(response);
        }

        return advertiseWithUsResponses;
    }

    @Override
    public Optional<AdsResponse> getAdsById(Long id) {
        // Retrieve the advertiseWithUs by its ID
        Optional<Ads> advertiseWithUsOptional = advertiseWithUsRepository.findById(id);
        // Check if the advertiseWithUs exists
        if (advertiseWithUsOptional.isPresent()) {
            // Convert Ads to AdsResponse if it exists
            Ads advertiseWithUs = advertiseWithUsOptional.get();
            AdsResponse advertiseWithUsResponse = new AdsResponse();
            advertiseWithUsResponse.setId(advertiseWithUs.getId());
            advertiseWithUsResponse.setStatus(advertiseWithUs.getStatus());
            advertiseWithUsResponse.setCreateDate(advertiseWithUs.getCreateDate());
            advertiseWithUsResponse.setLocal(advertiseWithUs.getLocal());
            advertiseWithUsResponse.setImage(advertiseWithUs.getImage());
            advertiseWithUsResponse.setUrl(advertiseWithUs.getUrl());
            // Set other necessary fields

            return Optional.of(advertiseWithUsResponse);
        } else {
            // Return an empty Optional if the advertiseWithUs doesn't exist
            return Optional.empty();
        }
    }

    @Override
    public void updateAds(Long id, AdsRequest advertiseWithUsRequest, MultipartFile imageFile) throws IOException {
        // Find the ad by ID
        Optional<Ads> advertiseWithUsOptional = advertiseWithUsRepository.findById(id);
        if (advertiseWithUsOptional.isPresent()) {
            Ads advertiseWithUs = advertiseWithUsOptional.get();
            // Update fields from request
            advertiseWithUs.setUrl(advertiseWithUsRequest.getUrl());
            advertiseWithUs.setStatus(advertiseWithUsRequest.getStatus());
            advertiseWithUs.setLocal(advertiseWithUsRequest.getLocal());
            advertiseWithUs.setUpdateBy(String.valueOf(getUserAccess.getUserAccess().getId()));
            advertiseWithUs.setUpdateDate(LocalDateTime.now());
            // Handle image update if new image is uploaded
            if (!imageFile.isEmpty()) {
                // Save the new image and get the path
                String imagePath = saveImage(imageFile);
                advertiseWithUs.setImage(imagePath);  // Update the image path in the ad
            }
            // Save the updated ad
            advertiseWithUsRepository.save(advertiseWithUs);
        } else {
            throw new CustomException("Ads not found with ID: " + id);
        }
    }

    // Image saving method
    public String saveImage(MultipartFile imageFile) throws IOException {
        // Generate a unique filename to avoid conflicts
        String filename = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
        Path imagePath = Paths.get(UPLOAD_DIRECTORY_ADS, filename);

        // Save the file
        Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

        // Return the relative image path
        return filename; // Adjust to your needs (e.g., URL or relative file path)
    }

    @Override
    public void updateAdsStatus(Long id, String status) {
        Optional<Ads> advertiseWithUsOptional = advertiseWithUsRepository.findById(id);
        if (advertiseWithUsOptional.isPresent()) {
            Ads advertiseWithUs = advertiseWithUsOptional.get();
            advertiseWithUs.setStatus(status); // Update the status (e.g., "0" for inactive)
            advertiseWithUs.setUpdateBy(String.valueOf(getUserAccess.getUserAccess().getId()));
            advertiseWithUs.setUpdateDate(LocalDateTime.now());
            advertiseWithUsRepository.save(advertiseWithUs); // Save the updated advertiseWithUs
        } else {
            throw new CustomException("Ads not found with ID: " + id);
        }
    }

}

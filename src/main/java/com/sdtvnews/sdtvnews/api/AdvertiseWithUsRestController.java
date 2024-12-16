package com.sdtvnews.sdtvnews.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdtvnews.sdtvnews.config.CustomException;
import com.sdtvnews.sdtvnews.config.exception.ResponseDTO;
import com.sdtvnews.sdtvnews.dto.request.AdsRequest;
import com.sdtvnews.sdtvnews.dto.response.AdsResponse;
import com.sdtvnews.sdtvnews.services.AdsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/advertiseWithUs")
public class AdvertiseWithUsRestController {

    private static final Logger log = LoggerFactory.getLogger(AdvertiseWithUsRestController.class);
    private final ObjectMapper objectMapper;

    private final AdsService advertiseWithUsService;

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAds(@RequestBody AdsRequest request) {
        try {
            log.info("Request to create Ads: {}", objectMapper.writeValueAsString(request));
            AdsRequest createdAds = advertiseWithUsService.createAds(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>("success", "Ads created successfully", createdAds));
        } catch (CustomException e) {
            log.warn("Ads creation failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDTO<>("error", e.getMessage(), request));
        } catch (Exception e) {
            log.error("Error processing request: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>("error", "An unexpected error occurred.", null));
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<?>> getAllAds() {
        try {
            log.info("Request to list all categories");
            List<AdsResponse> responseList = advertiseWithUsService.getAllAds();
            if (responseList.isEmpty()) {
                return ResponseEntity.ok(new ResponseDTO<>("success", "No categories found.", responseList));
            }
            return ResponseEntity.ok(new ResponseDTO<>("success", "Ads retrieved successfully", responseList));

        } catch (Exception e) {
            log.error("Error processing request: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>("error", "An unexpected error occurred.", null));
        }
    }

    @RequestMapping(value = "/find", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<?>> getAdsById(@RequestBody AdsRequest request) {
        try {
            log.info("Request to find Ads: {}", objectMapper.writeValueAsString(request));

            // Retrieve advertiseWithUs by ID
            Optional<AdsResponse> response = advertiseWithUsService.getAdsById(Long.valueOf(request.getId()));

            // Check if advertiseWithUs was found
            if (response.isPresent()) {
                return ResponseEntity.ok(new ResponseDTO<>("success", "Ads found by ID successfully", response.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO<>("error", "Ads not found.", null));
            }

        } catch (CustomException e) {
            log.warn("Error finding advertiseWithUs: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDTO<>("error", e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error processing request: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>("error", "An unexpected error occurred.", null));
        }
    }

    @RequestMapping(value = "/update-status", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<?>> updateAdsStatus(@RequestBody AdsRequest request) {
        try {
            log.info("Request to update status for Ads ID: {}", request.getId());
            advertiseWithUsService.updateAdsStatus(request.getId(), request.getStatus());
            return ResponseEntity.ok(new ResponseDTO<>("success", "Ads status updated successfully.", null));

        } catch (CustomException e) {
            log.warn("Error updating advertiseWithUs status: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDTO<>("error", e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error processing request: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>("error", "An unexpected error occurred.", null));
        }
    }

}

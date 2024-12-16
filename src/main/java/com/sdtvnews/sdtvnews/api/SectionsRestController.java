package com.sdtvnews.sdtvnews.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdtvnews.sdtvnews.config.CustomException;
import com.sdtvnews.sdtvnews.config.exception.ResponseDTO;
import com.sdtvnews.sdtvnews.dto.request.SectionsRequest;
import com.sdtvnews.sdtvnews.dto.response.SectionsResponse;
import com.sdtvnews.sdtvnews.services.SectionsService;
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
@RequestMapping("/api/sections")
public class SectionsRestController {

    private static final Logger log = LoggerFactory.getLogger(SectionsRestController.class);
    private final ObjectMapper objectMapper;

    private final SectionsService sectionsService;

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createSections(@RequestBody SectionsRequest request) {
        try {
            log.info("Request to create Sections: {}", objectMapper.writeValueAsString(request));
            SectionsRequest createdSections = sectionsService.createSections(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>("success", "Sections created successfully", createdSections));
        } catch (CustomException e) {
            log.warn("Sections creation failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDTO<>("error", e.getMessage(), request));
        } catch (Exception e) {
            log.error("Error processing request: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>("error", "An unexpected error occurred.", null));
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<?>> getAllSections() {
        try {
            log.info("Request to list all categories");
            List<SectionsResponse> responseList = sectionsService.getAllSections();
            if (responseList.isEmpty()) {
                return ResponseEntity.ok(new ResponseDTO<>("success", "No categories found.", responseList));
            }
            return ResponseEntity.ok(new ResponseDTO<>("success", "Sections retrieved successfully", responseList));

        } catch (Exception e) {
            log.error("Error processing request: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>("error", "An unexpected error occurred.", null));
        }
    }

    @RequestMapping(value = "/find", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<?>> getSectionsById(@RequestBody SectionsRequest request) {
        try {
            log.info("Request to find Sections: {}", objectMapper.writeValueAsString(request));

            // Retrieve sections by ID
            Optional<SectionsResponse> response = sectionsService.getSectionsById(Long.valueOf(request.getId()));

            // Check if sections was found
            if (response.isPresent()) {
                return ResponseEntity.ok(new ResponseDTO<>("success", "Sections found by ID successfully", response.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO<>("error", "Sections not found.", null));
            }

        } catch (CustomException e) {
            log.warn("Error finding sections: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDTO<>("error", e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error processing request: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>("error", "An unexpected error occurred.", null));
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<?>> updateSections(@RequestBody SectionsRequest request) {
        try {
            log.info("Request to update Sections: {}", objectMapper.writeValueAsString(request));
            sectionsService.updateSections(Long.valueOf(request.getId()), request);
            return ResponseEntity.ok(new ResponseDTO<>("success", "Sections updated successfully", null));
        } catch (CustomException e) {
            log.warn("Error updating sections: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDTO<>("error", e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error processing request: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>("error", "An unexpected error occurred.", null));
        }
    }


    @RequestMapping(value = "/update-status", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<?>> updateSectionsStatus(@RequestBody SectionsRequest request) {
        try {
            log.info("Request to update status for Sections ID: {}", request.getId());
            sectionsService.updateSectionsStatus(request.getId(), request.getStatus());
            return ResponseEntity.ok(new ResponseDTO<>("success", "Sections status updated successfully.", null));

        } catch (CustomException e) {
            log.warn("Error updating sections status: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDTO<>("error", e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error processing request: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>("error", "An unexpected error occurred.", null));
        }
    }

}

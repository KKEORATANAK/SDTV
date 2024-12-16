package com.sdtvnews.sdtvnews.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdtvnews.sdtvnews.config.CustomException;
import com.sdtvnews.sdtvnews.config.exception.ResponseDTO;
import com.sdtvnews.sdtvnews.dto.request.FollowRequest;
import com.sdtvnews.sdtvnews.dto.response.FollowResponse;
import com.sdtvnews.sdtvnews.services.FollowService;
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
@RequestMapping("/api/follow")
public class FollowRestController {

    private static final Logger log = LoggerFactory.getLogger(FollowRestController.class);
    private final ObjectMapper objectMapper;

    private final FollowService followService;

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createFollow(@RequestBody FollowRequest request) {
        try {
            log.info("Request to create Follow: {}", objectMapper.writeValueAsString(request));
            FollowRequest createdFollow = followService.createFollow(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>("success", "Follow created successfully", createdFollow));
        } catch (CustomException e) {
            log.warn("Follow creation failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDTO<>("error", e.getMessage(), request));
        } catch (Exception e) {
            log.error("Error processing request: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>("error", "An unexpected error occurred.", null));
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<?>> getAllFollow() {
        try {
            log.info("Request to list all follow");
            List<FollowResponse> responseList = followService.getAllFollow();
            if (responseList.isEmpty()) {
                return ResponseEntity.ok(new ResponseDTO<>("success", "No follow found.", responseList));
            }
            return ResponseEntity.ok(new ResponseDTO<>("success", "Follow retrieved successfully", responseList));

        } catch (Exception e) {
            log.error("Error processing request: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>("error", "An unexpected error occurred.", null));
        }
    }

    @RequestMapping(value = "/find", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<?>> getFollowById(@RequestBody FollowRequest request) {
        try {
            log.info("Request to find Follow: {}", objectMapper.writeValueAsString(request));

            // Retrieve follow by ID
            Optional<FollowResponse> response = followService.getFollowById(Long.valueOf(request.getId()));

            // Check if follow was found
            if (response.isPresent()) {
                return ResponseEntity.ok(new ResponseDTO<>("success", "Follow found by ID successfully", response.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO<>("error", "Follow not found.", null));
            }

        } catch (CustomException e) {
            log.warn("Error finding follow: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDTO<>("error", e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error processing request: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>("error", "An unexpected error occurred.", null));
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<?>> updateFollow(@RequestBody FollowRequest request) {
        try {
            log.info("Request to update Follow: {}", objectMapper.writeValueAsString(request));
            followService.updateFollow(Long.valueOf(request.getId()), request);
            return ResponseEntity.ok(new ResponseDTO<>("success", "Follow updated successfully", null));
        } catch (CustomException e) {
            log.warn("Error updating follow: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDTO<>("error", e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error processing request: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>("error", "An unexpected error occurred.", null));
        }
    }


    @RequestMapping(value = "/update-status", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<?>> updateFollowStatus(@RequestBody FollowRequest request) {
        try {
            log.info("Request to update status for Follow ID: {}", request.getId());
            followService.updateFollowStatus(request.getId(), request.getStatus());
            return ResponseEntity.ok(new ResponseDTO<>("success", "Follow status updated successfully.", null));

        } catch (CustomException e) {
            log.warn("Error updating follow status: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDTO<>("error", e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error processing request: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>("error", "An unexpected error occurred.", null));
        }
    }





}

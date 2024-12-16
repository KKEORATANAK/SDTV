package com.sdtvnews.sdtvnews.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdtvnews.sdtvnews.config.CustomException;
import com.sdtvnews.sdtvnews.config.exception.ResponseDTO;
import com.sdtvnews.sdtvnews.dto.response.CategoryResponse;
import com.sdtvnews.sdtvnews.dto.request.CategoryRequest;
import com.sdtvnews.sdtvnews.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CatgoryRestController {

    private static final Logger log = LoggerFactory.getLogger(CatgoryRestController.class);
    private final ObjectMapper objectMapper;

    private final CategoryService categoryService;

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCategory(@RequestBody CategoryRequest request) {
        try {
            log.info("Request to create Category: {}", objectMapper.writeValueAsString(request));
            CategoryRequest createdCategory = categoryService.createCategory(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>("success", "Category created successfully", createdCategory));
        } catch (CustomException e) {
            log.warn("Category creation failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDTO<>("error", e.getMessage(), request));
        } catch (Exception e) {
            log.error("Error processing request: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>("error", "An unexpected error occurred.", null));
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<?>> getAllCategories() {
        try {
            log.info("Request to list all categories");
            List<CategoryResponse> responseList = categoryService.getAllCategories();
            if (responseList.isEmpty()) {
                return ResponseEntity.ok(new ResponseDTO<>("success", "No categories found.", responseList));
            }
            return ResponseEntity.ok(new ResponseDTO<>("success", "Categories retrieved successfully", responseList));

        } catch (Exception e) {
            log.error("Error processing request: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>("error", "An unexpected error occurred.", null));
        }
    }

    @RequestMapping(value = "/find", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<?>> getCategoryById(@RequestBody CategoryRequest request) {
        try {
            log.info("Request to find Category: {}", objectMapper.writeValueAsString(request));

            // Retrieve category by ID
            Optional<CategoryResponse> response = categoryService.getCategoryById(Long.valueOf(request.getId()));

            // Check if category was found
            if (response.isPresent()) {
                return ResponseEntity.ok(new ResponseDTO<>("success", "Category found by ID successfully", response.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO<>("error", "Category not found.", null));
            }

        } catch (CustomException e) {
            log.warn("Error finding category: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDTO<>("error", e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error processing request: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>("error", "An unexpected error occurred.", null));
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<?>> updateCategory(@RequestBody CategoryRequest request) {
        try {
            log.info("Request to update Category: {}", objectMapper.writeValueAsString(request));
            categoryService.updateCategory(Long.valueOf(request.getId()), request);
            return ResponseEntity.ok(new ResponseDTO<>("success", "Category updated successfully", null));
        } catch (CustomException e) {
            log.warn("Error updating category: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDTO<>("error", e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error processing request: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>("error", "An unexpected error occurred.", null));
        }
    }


    @RequestMapping(value = "/update-status", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<?>> updateCategoryStatus(@RequestBody CategoryRequest request) {
        try {
            log.info("Request to update status for Category ID: {}", request.getId());
            categoryService.updateCategoryStatus(request.getId(), request.getStatus());
            return ResponseEntity.ok(new ResponseDTO<>("success", "Category status updated successfully.", null));

        } catch (CustomException e) {
            log.warn("Error updating category status: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDTO<>("error", e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error processing request: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>("error", "An unexpected error occurred.", null));
        }
    }





}

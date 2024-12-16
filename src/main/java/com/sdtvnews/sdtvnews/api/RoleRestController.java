package com.sdtvnews.sdtvnews.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdtvnews.sdtvnews.config.CustomException;
import com.sdtvnews.sdtvnews.config.exception.ResponseDTO;
import com.sdtvnews.sdtvnews.dto.request.RoleRequest;
import com.sdtvnews.sdtvnews.dto.response.RoleResponse;
import com.sdtvnews.sdtvnews.services.RoleService;
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
@RequestMapping("/api/role")
public class RoleRestController {

    private static final Logger log = LoggerFactory.getLogger(RoleRestController.class);
    private final ObjectMapper objectMapper;

    private final RoleService roleService;

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createRole(@RequestBody RoleRequest request) {
        try {
            log.info("Request to create Role: {}", objectMapper.writeValueAsString(request));
            RoleRequest createdRole = roleService.createRole(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>("success", "Role created successfully", createdRole));
        } catch (CustomException e) {
            log.warn("Role creation failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDTO<>("error", e.getMessage(), request));
        } catch (Exception e) {
            log.error("Error processing request: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>("error", "An unexpected error occurred.", null));
        }
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<?>> getAllRole() {
        try {
            log.info("Request to list all role");
            List<RoleResponse> responseList = roleService.getAllRole();
            if (responseList.isEmpty()) {
                return ResponseEntity.ok(new ResponseDTO<>("success", "No role found.", responseList));
            }
            return ResponseEntity.ok(new ResponseDTO<>("success", "Role retrieved successfully", responseList));

        } catch (Exception e) {
            log.error("Error processing request: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>("error", "An unexpected error occurred.", null));
        }
    }

    @RequestMapping(value = "/find", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<?>> getRoleById(@RequestBody RoleRequest request) {
        try {
            log.info("Request to find Role: {}", objectMapper.writeValueAsString(request));

            // Retrieve role by ID
            Optional<RoleResponse> response = roleService.getRoleById(Long.valueOf(request.getId()));

            // Check if role was found
            if (response.isPresent()) {
                return ResponseEntity.ok(new ResponseDTO<>("success", "Role found by ID successfully", response.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO<>("error", "Role not found.", null));
            }

        } catch (CustomException e) {
            log.warn("Error finding role: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDTO<>("error", e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error processing request: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>("error", "An unexpected error occurred.", null));
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<?>> updateRole(@RequestBody RoleRequest request) {
        try {
            log.info("Request to update Role: {}", objectMapper.writeValueAsString(request));
            roleService.updateRole(Long.valueOf(request.getId()), request);
            return ResponseEntity.ok(new ResponseDTO<>("success", "Role updated successfully", null));
        } catch (CustomException e) {
            log.warn("Error updating role: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDTO<>("error", e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error processing request: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>("error", "An unexpected error occurred.", null));
        }
    }


    @RequestMapping(value = "/update-status", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO<?>> updateRoleStatus(@RequestBody RoleRequest request) {
        try {
            log.info("Request to update status for Role ID: {}", request.getId());
            roleService.updateRoleStatus(request.getId(), request.getStatus());
            return ResponseEntity.ok(new ResponseDTO<>("success", "Role status updated successfully.", null));

        } catch (CustomException e) {
            log.warn("Error updating role status: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDTO<>("error", e.getMessage(), null));
        } catch (Exception e) {
            log.error("Error processing request: {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO<>("error", "An unexpected error occurred.", null));
        }
    }

}

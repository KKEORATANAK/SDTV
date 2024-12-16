package com.sdtvnews.sdtvnews.servicesImp;

import com.sdtvnews.sdtvnews.config.CustomException;
import com.sdtvnews.sdtvnews.dto.request.RoleRequest;
import com.sdtvnews.sdtvnews.dto.response.RoleResponse;
import com.sdtvnews.sdtvnews.entity.Role;
import com.sdtvnews.sdtvnews.repository.RoleRepository;
import com.sdtvnews.sdtvnews.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImp implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public RoleRequest createRole(RoleRequest roleRequest) {
        // Check if the role already exists
        List<Role> existingCategories = roleRepository.findAll();

        for (Role role : existingCategories) {
            if (role.getName().equalsIgnoreCase(roleRequest.getName())) {
                // Return a message if the role already exists
                throw new CustomException("Role with the name '" + roleRequest.getName() + "' already exists.");
            }
        }

        // Create a new Role if it doesn't exist
        Role newRole = new Role();
        newRole.setName(roleRequest.getName());
        newRole.setDescription(roleRequest.getDescription());
        newRole.setStatus("1");//1=active;0=dis-active
        newRole.setCreateBy(roleRequest.getCreateBy());
        newRole.setCreateDate(LocalDateTime.now());
        // Save the new role
        roleRepository.save(newRole);

        return roleRequest; // or convert and return the saved entity as needed
    }

    @Override
    public List<RoleResponse> getAllRole() {
        List<Role> categories = roleRepository.findAll(); // Retrieve all role from the repository
        List<RoleResponse> roleResponses = new ArrayList<>();

        for (Role role : categories) {
            RoleResponse response = new RoleResponse();
            response.setId(role.getId());
            response.setName(role.getName());
            response.setDescription(role.getDescription());
            response.setStatus(role.getStatus());
            response.setCreateDate(role.getCreateDate());
            // Set other necessary fields

            roleResponses.add(response);
        }

        return roleResponses;
    }

    @Override
    public List<RoleResponse> getActiveRole() {
        List<Role> categories = roleRepository.lstActiveRole(); // Retrieve all role from the repository
        List<RoleResponse> roleResponses = new ArrayList<>();

        for (Role role : categories) {
            RoleResponse response = new RoleResponse();
            response.setId(role.getId());
            response.setName(role.getName());
            response.setDescription(role.getDescription());
            response.setStatus(role.getStatus());
            response.setCreateDate(role.getCreateDate());
            // Set other necessary fields

            roleResponses.add(response);
        }

        return roleResponses;
    }


    @Override
    public Optional<RoleResponse> getRoleById(Long id) {
        // Retrieve the role by its ID
        Optional<Role> roleOptional = roleRepository.findById(id);

        // Check if the role exists
        if (roleOptional.isPresent()) {
            // Convert Role to RoleResponse if it exists
            Role role = roleOptional.get();
            RoleResponse roleResponse = new RoleResponse();
            roleResponse.setId(role.getId());
            roleResponse.setName(role.getName());
            roleResponse.setDescription(role.getDescription());
            roleResponse.setStatus(role.getStatus());
            roleResponse.setCreateDate(role.getCreateDate());
            // Set other necessary fields

            return Optional.of(roleResponse);
        } else {
            // Return an empty Optional if the role doesn't exist
            return Optional.empty();
        }
    }

    @Override
    public void updateRole(Long id, RoleRequest roleRequest) {
        // Find the role by its ID
        Optional<Role> roleOptional = roleRepository.findById(id);

        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            // Update the role details
            role.setName(roleRequest.getName());
            role.setDescription(roleRequest.getDescription());
            role.setStatus(roleRequest.getStatus());
            role.setUpdateBy(roleRequest.getUpdateBy());
            role.setUpdateDate(LocalDateTime.now());

            // Save the updated role
            roleRepository.save(role);
        } else {
            throw new CustomException("Role not found with ID: " + id);
        }
    }


    @Override
    public void updateRoleStatus(Long id, String status) {
        Optional<Role> roleOptional = roleRepository.findById(id);

        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            role.setStatus(status); // Update the status (e.g., "0" for inactive)
            roleRepository.save(role); // Save the updated role
        } else {
            throw new CustomException("Role not found with ID: " + id);
        }
    }

    public boolean isNameDuplicate(String name) {
        // Check if the title exists in the database
        return roleRepository.existsByName(name);
    }

}

package com.sdtvnews.sdtvnews.controller;

import com.sdtvnews.sdtvnews.config.CustomException;
import com.sdtvnews.sdtvnews.dto.request.RoleRequest;
import com.sdtvnews.sdtvnews.dto.response.RoleResponse;
import com.sdtvnews.sdtvnews.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @GetMapping("/index")
    public String indexPage(Model model) {

        List<RoleResponse>lstResponsesData= roleService.getAllRole();
        // Retrieve the authorities of the logged-in user
        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        // Check for admin role
        boolean isAdmin = authorities.stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        model.addAttribute("lstResponsesData",lstResponsesData);
        return "dashboard/role";  // Return login page
    }


    @PostMapping("/create")
    public String createRole(@ModelAttribute RoleRequest request, RedirectAttributes redirectAttributes) {
        try {
            // Attempt to create the role
            roleService.createRole(request);
            redirectAttributes.addFlashAttribute("create", "Role created successfully!");
        } catch (CustomException e) {
            // Handle the case where the role already exists or another error occurs
            redirectAttributes.addFlashAttribute("warning", e.getMessage());
        }
        return "redirect:/role/index"; // Redirect to the role index page
    }

    @GetMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable String id) {
        try {

            Optional<RoleResponse> response = roleService.getRoleById(Long.valueOf(id));
            // Check if the response is present
            if (response.isPresent()) {
                return ResponseEntity.ok(response.get()); // Return the found section
            } else {
                return ResponseEntity.notFound().build(); // Return 404 if not found
            }
        } catch (CustomException e) {
            // Log the exception if needed
            // You can return a custom error message or status if desired
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (NumberFormatException e) {
            // Handle case where id is not a valid Long
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/update")
    public String updateRole(@ModelAttribute("role") RoleRequest role, RedirectAttributes redirectAttributes) {
        try {
            roleService.updateRole(Long.valueOf(role.getId()), role);
            redirectAttributes.addFlashAttribute("update", "Role update successfully!");
        } catch (CustomException e) {
            // Handle the case where the section already exists
            redirectAttributes.addFlashAttribute("warning", e.getMessage()); // Add warning message
        }
        return "redirect:/role/index"; // Redirect to the role page
    }

    @GetMapping("/activate/{id}")
    public String activateRole(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            // Call your service to activate the section
            roleService.updateRoleStatus(Long.valueOf(id),"1");
            redirectAttributes.addFlashAttribute("active", "Role status activate updated successfully.");
        } catch (CustomException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to activate section: " + e.getMessage());
        }
        return "redirect:/role/index"; // Redirect to the role index page
    }

    @GetMapping("/dis-activate/{id}")
    public String DisActivateRole(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            // Call your service to activate the section
            roleService.updateRoleStatus(Long.valueOf(id),"0");
            redirectAttributes.addFlashAttribute("active", "Role status dis-activate updated successfully.");
        } catch (CustomException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to activate section: " + e.getMessage());
        }
        return "redirect:/role/index"; // Redirect to the role index page
    }

    @GetMapping("/checkNameDuplicate")
    public ResponseEntity<Map<String, Boolean>> checkTitleDuplicate(@RequestParam("name") String name) {
        boolean isDuplicate = roleService.isNameDuplicate(name); // Call service to check duplication

        // Prepare response
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);
        return ResponseEntity.ok(response);
    }

}

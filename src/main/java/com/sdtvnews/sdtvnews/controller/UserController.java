package com.sdtvnews.sdtvnews.controller;

import com.sdtvnews.sdtvnews.config.CustomException;
import com.sdtvnews.sdtvnews.dto.request.UserRequest;
import com.sdtvnews.sdtvnews.dto.response.RoleResponse;
import com.sdtvnews.sdtvnews.dto.response.UserResponse;
import com.sdtvnews.sdtvnews.services.RoleService;
import com.sdtvnews.sdtvnews.services.UserService;
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
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;

    @GetMapping("/index")
    public String indexPage(Model model) {
        List<UserResponse>lstResponsesData= userService.getAllUser();
        List<RoleResponse>roleResponses=roleService.getActiveRole();

        // Retrieve the authorities of the logged-in user
        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        // Check for admin role
        boolean isAdmin = authorities.stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        model.addAttribute("lstResponsesData",lstResponsesData);
        model.addAttribute("roleResponses",roleResponses);
        return "dashboard/user";  // Return login page
    }


    @PostMapping("/create")
    public String createUser(@ModelAttribute UserRequest request, RedirectAttributes redirectAttributes) {
        try {
            // Attempt to create the user
            userService.createUser(request);
            redirectAttributes.addFlashAttribute("create", "User created successfully!");
        } catch (CustomException e) {
            // Handle the case where the user already exists or another error occurs
            redirectAttributes.addFlashAttribute("warning", e.getMessage());
        }
        return "redirect:/user/index"; // Redirect to the user index page
    }

    @GetMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        try {
            Optional<UserResponse> response = userService.getUserById(Long.valueOf(id));
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
    public String updateUser(@ModelAttribute("user") UserRequest user, RedirectAttributes redirectAttributes){
        try {
            userService.updateUser(Long.valueOf(user.getId()), user);
            redirectAttributes.addFlashAttribute("update", "User update successfully!");
        } catch (CustomException e) {
            // Handle the case where the section already exists
            redirectAttributes.addFlashAttribute("warning", e.getMessage()); // Add warning message
        }
        return "redirect:/user/index"; // Redirect to the user page
    }

    @GetMapping("/activate/{id}")
    public String activateUser(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            // Call your service to activate the section
            userService.updateUserStatus(Long.valueOf(id),"1");
            redirectAttributes.addFlashAttribute("active", "User status activate updated successfully.");
        } catch (CustomException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to activate section: " + e.getMessage());
        }
        return "redirect:/user/index"; // Redirect to the user index page
    }

    @GetMapping("/dis-activate/{id}")
    public String DisActivateUser(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            // Call your service to activate the section
            userService.updateUserStatus(Long.valueOf(id),"0");
            redirectAttributes.addFlashAttribute("active", "User status dis-activate updated successfully.");
        } catch (CustomException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to activate section: " + e.getMessage());
        }
        return "redirect:/user/index"; // Redirect to the user index page
    }

    @GetMapping("/checkNameDuplicate")
    public ResponseEntity<Map<String, Boolean>> checkTitleDuplicate(@RequestParam("username") String username) {
        boolean isDuplicate = userService.isNameDuplicate(username); // Call service to check duplication
        // Prepare response
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteArticle(@PathVariable Long id) {
        try {
            // Your delete logic here
            userService.deleteUser(id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user");
        }
    }

}

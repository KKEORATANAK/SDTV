package com.sdtvnews.sdtvnews.controller;

import com.sdtvnews.sdtvnews.config.CustomException;
import com.sdtvnews.sdtvnews.dto.request.CategoryRequest;
import com.sdtvnews.sdtvnews.dto.request.SortedItem;
import com.sdtvnews.sdtvnews.dto.response.CategoryResponse;
import com.sdtvnews.sdtvnews.repository.CategoryRepository;
import com.sdtvnews.sdtvnews.services.CategoryService;
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
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/index")
    public String indexPage(Model model) {

        List<CategoryResponse>lstResponsesData= categoryService.getAllCategories();

        // Retrieve the authorities of the logged-in user
        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        // Check for admin role
        boolean isAdmin = authorities.stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        model.addAttribute("lstResponsesData",lstResponsesData);
        return "dashboard/category";  // Return login page
    }

    @PostMapping("/create")
    public String createCategory(@ModelAttribute CategoryRequest request, RedirectAttributes redirectAttributes) {
        try {
            // Attempt to create the category
            categoryService.createCategory(request);
            redirectAttributes.addFlashAttribute("create", "Category created successfully!");
        } catch (CustomException e) {
            // Handle the case where the category already exists or another error occurs
            redirectAttributes.addFlashAttribute("warning", e.getMessage());
        }
        return "redirect:/category/index"; // Redirect to the category index page
    }

    @GetMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable String id) {
        try {
            Optional<CategoryResponse> response = categoryService.getCategoryById(Long.valueOf(id));
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
    public String updateCategory(@ModelAttribute("category") CategoryRequest category, RedirectAttributes redirectAttributes) {
        try {
            categoryService.updateCategory(Long.valueOf(category.getId()), category);
            redirectAttributes.addFlashAttribute("update", "Category update successfully!");
        } catch (CustomException e) {
            // Handle the case where the section already exists
            redirectAttributes.addFlashAttribute("warning", e.getMessage()); // Add warning message
        }
        return "redirect:/category/index"; // Redirect to the category page
    }

    @GetMapping("/activate/{id}")
    public String activateCategory(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            // Call your service to activate the section
            categoryService.updateCategoryStatus(Long.valueOf(id),"1");
            redirectAttributes.addFlashAttribute("active", "Category status activate updated successfully.");
        } catch (CustomException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to activate section: " + e.getMessage());
        }
        return "redirect:/category/index"; // Redirect to the category index page
    }

    @GetMapping("/dis-activate/{id}")
    public String DisActivateCategory(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            // Call your service to activate the section
            categoryService.updateCategoryStatus(Long.valueOf(id),"0");
            redirectAttributes.addFlashAttribute("active", "Category status dis-activate updated successfully.");
        } catch (CustomException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to activate section: " + e.getMessage());
        }
        return "redirect:/category/index"; // Redirect to the category index page
    }

    @GetMapping("/checkNameDuplicate")
    public ResponseEntity<Map<String, Boolean>> checkTitleDuplicate(@RequestParam("name") String name) {
        boolean isDuplicate = categoryService.isNameDuplicate(name); // Call service to check duplication
        // Prepare response
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/updateSorted" )
    @ResponseBody
    public ResponseEntity handleSortedData(@RequestBody List<SortedItem> sortedItems) {
        categoryService.handleSortedData(sortedItems);
        // Return a success response
        return ResponseEntity.ok("Data received successfully");
    }

}

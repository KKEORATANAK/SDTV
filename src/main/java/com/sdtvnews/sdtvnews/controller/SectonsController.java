package com.sdtvnews.sdtvnews.controller;

import com.sdtvnews.sdtvnews.config.CustomException;
import com.sdtvnews.sdtvnews.dto.request.SectionsRequest;
import com.sdtvnews.sdtvnews.dto.response.SectionsResponse;
import com.sdtvnews.sdtvnews.services.SectionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/sections")
public class SectonsController {

    @Autowired
    SectionsService sectionsService;

    @GetMapping("/index")
    public String indexPage(Model model) {

        List<SectionsResponse>lstResponsesData= sectionsService.getAllSections();
        // Retrieve the authorities of the logged-in user
        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        // Check for admin role
        boolean isAdmin = authorities.stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        model.addAttribute("lstResponsesData",lstResponsesData);
        return "dashboard/sections";  // Return login page
    }

    @PostMapping("/create")
    public String createSections(@ModelAttribute("section") SectionsRequest section, RedirectAttributes redirectAttributes) {
        try {
            sectionsService.createSections(section);  // Attempt to create section
            redirectAttributes.addFlashAttribute("create", "Section created successfully!");
        } catch (CustomException e) {
            // Handle the case where the section already exists
            redirectAttributes.addFlashAttribute("warning", e.getMessage()); // Add warning message
        }
        return "redirect:/sections/index"; // Redirect to the sections page
    }

    @GetMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<SectionsResponse> getSectionsById(@PathVariable String id) {
        try {
            Optional<SectionsResponse> response = sectionsService.getSectionsById(Long.valueOf(id));

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
    public String updateSections(@ModelAttribute("section") SectionsRequest section, RedirectAttributes redirectAttributes) {
        try {
            sectionsService.updateSections(Long.valueOf(section.getId()), section);
            redirectAttributes.addFlashAttribute("update", "Section update successfully!");
        } catch (CustomException e) {
            // Handle the case where the section already exists
            redirectAttributes.addFlashAttribute("warning", e.getMessage()); // Add warning message
        }
        return "redirect:/sections/index"; // Redirect to the sections page
    }

    @GetMapping("/activate/{id}")
    public String activateSection(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            System.out.println("string id:" + id);
            // Call your service to activate the section
            sectionsService.updateSectionsStatus(Long.valueOf(id),"1");
            redirectAttributes.addFlashAttribute("active", "Sections status activate updated successfully.");
        } catch (CustomException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to activate section: " + e.getMessage());
        }
        return "redirect:/sections/index"; // Redirect to the sections index page
    }

    @GetMapping("/dis-activate/{id}")
    public String DisActivateSection(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            // Call your service to activate the section
            sectionsService.updateSectionsStatus(Long.valueOf(id),"0");
            redirectAttributes.addFlashAttribute("active", "Sections status dis-activate updated successfully.");
        } catch (CustomException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to activate section: " + e.getMessage());
        }
        return "redirect:/sections/index"; // Redirect to the sections index page
    }




}

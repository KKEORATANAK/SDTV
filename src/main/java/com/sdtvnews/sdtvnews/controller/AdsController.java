package com.sdtvnews.sdtvnews.controller;

import com.sdtvnews.sdtvnews.config.CustomException;
import com.sdtvnews.sdtvnews.dto.request.AdsRequest;
import com.sdtvnews.sdtvnews.dto.response.AdsResponse;
import com.sdtvnews.sdtvnews.services.AdsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ads")
public class AdsController {

    @Autowired
    AdsService adsService;

    @GetMapping("/index")
    public String indexPage(Model model) {
        List<AdsResponse>lstResponsesData= adsService.getAllAds();
        // Retrieve the authorities of the logged-in user
        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        // Check for admin role
        boolean isAdmin = authorities.stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("lstResponsesData",lstResponsesData);
        return "dashboard/ads";
    }

    @PostMapping("/create")
    public String createAds(@ModelAttribute AdsRequest request, RedirectAttributes redirectAttributes)throws IOException{
        try {
            adsService.createAds(request);
            redirectAttributes.addFlashAttribute("create", "Create ADS successfully!");
        } catch (CustomException e) {
            redirectAttributes.addFlashAttribute("warning", e.getMessage());
        }
        return "redirect:/ads/index";
    }

    @GetMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<AdsResponse> getAdsById(@PathVariable String id) {
        try {
            Optional<AdsResponse> response = adsService.getAdsById(Long.valueOf(id));
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
    public String updateAds(@ModelAttribute AdsRequest ads,
                            @RequestParam("image") MultipartFile imageFile,
                            RedirectAttributes redirectAttributes) {
        try {
            adsService.updateAds(Long.valueOf(ads.getId()), ads,imageFile);
            redirectAttributes.addFlashAttribute("update", "Ads update successfully!");
        } catch (CustomException | IOException e) {
            // Handle the case where the section already exists
            redirectAttributes.addFlashAttribute("warning", e.getMessage()); // Add warning message
        }
        return "redirect:/ads/index"; // Redirect to the ads page
    }

    @GetMapping("/activate/{id}")
    public String activateAds(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            adsService.updateAdsStatus(Long.valueOf(id),"1");
            redirectAttributes.addFlashAttribute("active", "ADS status activate updated successfully.");
        } catch (CustomException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to activate section: " + e.getMessage());
        }
        return "redirect:/ads/index"; // Redirect to the ads index page
    }

    @GetMapping("/dis-activate/{id}")
    public String DisActivateAds(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            // Call your service to activate the section
            adsService.updateAdsStatus(Long.valueOf(id),"0");
            redirectAttributes.addFlashAttribute("active", "ADS status dis-activate updated successfully.");
        } catch (CustomException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to activate section: " + e.getMessage());
        }
        return "redirect:/ads/index"; // Redirect to the ads index page
    }

}

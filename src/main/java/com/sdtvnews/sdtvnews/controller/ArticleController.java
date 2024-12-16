package com.sdtvnews.sdtvnews.controller;

import com.sdtvnews.sdtvnews.config.CustomException;
import com.sdtvnews.sdtvnews.config.security.CustomUserDetails;
import com.sdtvnews.sdtvnews.dto.ListArticleDTO;
import com.sdtvnews.sdtvnews.dto.request.NewsArticleRequest;
import com.sdtvnews.sdtvnews.dto.response.CategoryResponse;
import com.sdtvnews.sdtvnews.dto.response.UserResponse;
import com.sdtvnews.sdtvnews.entity.NewsArticle;
import com.sdtvnews.sdtvnews.services.CategoryService;
import com.sdtvnews.sdtvnews.services.NewsArticleService;
import com.sdtvnews.sdtvnews.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    NewsArticleService newsArticleService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    UserService userService;

    @GetMapping("/new-article")
    public String createArticle(Model model) {
        List<CategoryResponse>lstCategory= categoryService.getActiveCategories();
        // Retrieve the authorities of the logged-in user
        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        // Check for admin role
        boolean isAdmin = authorities.stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("lstCategory",lstCategory);
        return "dashboard/new-article";  // Return login page
    }

    @GetMapping("/list-article")
    public String listArticle(Model model) {
        List<ListArticleDTO>lstdata = newsArticleService.getAllArticles();
        List<CategoryResponse>lstCategory= categoryService.getActiveCategories();
        List<UserResponse>lstUser= userService.getActiveUser();
        // Retrieve the authorities of the logged-in user
        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        // Check for admin role
        boolean isAdmin = authorities.stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("lstCategory",lstCategory);
        model.addAttribute("lstUser",lstUser);
        model.addAttribute("lstdata",lstdata);

        return "dashboard/list-article";
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteArticle(@PathVariable Long id) {
        try {
            // Your delete logic here
            newsArticleService.deleteArticle(id);
            return ResponseEntity.ok("Article deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete article");
        }
    }

    @GetMapping("/list-article-search")
    public String listArticleSearch(@RequestParam("keyWord") String keyWord,Model model) {
        List<ListArticleDTO>lstdata = newsArticleService.getAllArticlesSearch(keyWord);
        int count = lstdata.size();
        // Retrieve the authorities of the logged-in user
        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        // Check for admin role
        boolean isAdmin = authorities.stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("lstdata",lstdata);
        model.addAttribute("keyWord",keyWord);
        model.addAttribute("count",count);

        return "dashboard/list-article-search";
    }

    @GetMapping("/find-by/{id}")
    public String findById(@PathVariable String id, Model model) {
        NewsArticle lstdata = newsArticleService.getArticleById(Long.valueOf(id));
        List<CategoryResponse>lstCategory= categoryService.getActiveCategories();
        // Retrieve the authorities of the logged-in user
        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        // Check for admin role
        boolean isAdmin = authorities.stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("lstCategory",lstCategory);
        model.addAttribute("categoryId", lstdata.getCateId());
        model.addAttribute("lstdata",lstdata);
        //model.addAttribute("responses",responses);
        return "dashboard/edit-article";  // Return login page
    }

    @PostMapping("/create")
    public String createSections(@ModelAttribute("article") NewsArticleRequest request, RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                Long userId = userDetails.getId(); // Retrieve user ID from the authenticated user

                request.setUserId(userId); // Set the user ID in the request
            }
            newsArticleService.createArticle(request);  // Attempt to create section
            redirectAttributes.addFlashAttribute("create", "Article created successfully!");
        } catch (Exception e) {
            // Handle the case where the section already exists
            redirectAttributes.addFlashAttribute("warning", e.getMessage()); // Add warning message
        }
        return "redirect:/article/list-article"; // Redirect to the sections page
    }

    @PostMapping("/update")
    public String updateSections(@ModelAttribute("article") NewsArticleRequest request, RedirectAttributes redirectAttributes) {
        try {
            newsArticleService.updateSections(Long.valueOf(request.getId()),request);  // Attempt to create section
            redirectAttributes.addFlashAttribute("update", "Article update successfully!");
        } catch (Exception e) {
            // Handle the case where the section already exists
            redirectAttributes.addFlashAttribute("warning", e.getMessage()); // Add warning message
        }
        return "redirect:/article/list-article"; // Redirect to the sections page
    }

    @GetMapping("/checkTitleDuplicate")
    public ResponseEntity<Map<String, Boolean>> checkTitleDuplicate(@RequestParam("title") String title) {
        boolean isDuplicate = newsArticleService.isTitleDuplicate(title); // Call service to check duplication
        // Prepare response
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/activate/{id}")
    public String activateAds(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            newsArticleService.updateArticleStatus(Long.valueOf(id),"1");
            redirectAttributes.addFlashAttribute("active", "Article status activate updated successfully.");
        } catch (CustomException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to activate section: " + e.getMessage());
        }
        return "redirect:/article/list-article"; // Redirect to the  index page
    }

    @GetMapping("/dis-activate/{id}")
    public String DisActivateAds(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            // Call your service to activate the section
            newsArticleService.updateArticleStatus(Long.valueOf(id),"0");
            redirectAttributes.addFlashAttribute("active", "Article status dis-activate updated successfully.");
        } catch (CustomException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to activate section: " + e.getMessage());
        }
        return "redirect:/article/list-article"; // Redirect to the  index page
    }

    @GetMapping("/lst/search")
    public String listSearch(
            Model model,
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            @RequestParam(value = "categoryId", required = false) String categoryId,
            @RequestParam(value = "marquee", required = false) String marquee) {

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fromDateConvert = null;
        String toDateConvert = null;
        // Retrieve the authorities of the logged-in user
        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        // Check for admin role
        boolean isAdmin = authorities.stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        // Convert fromDate if not null
        if (fromDate != null && !fromDate.isEmpty()) {
            LocalDate fromDateParsed = LocalDate.parse(fromDate, inputFormatter);
            fromDateConvert = fromDateParsed.format(outputFormatter);
        }

        // Convert toDate if not null
        if (toDate != null && !toDate.isEmpty()) {
            LocalDate toDateParsed = LocalDate.parse(toDate, inputFormatter);
            toDateConvert = toDateParsed.format(outputFormatter);
        }

        List<ListArticleDTO>lstdata = newsArticleService.getSearchArticles(userId,fromDateConvert,toDateConvert,categoryId,marquee);

        List<CategoryResponse>lstCategory= categoryService.getActiveCategories();
        List<UserResponse>lstUser= userService.getActiveUser();

        model.addAttribute("lstCategory",lstCategory);
        model.addAttribute("lstUser",lstUser);
        model.addAttribute("userId", userId);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("marquee", marquee);

        model.addAttribute("lstdata",lstdata);
        // Handle search logic using query parameters
        return "dashboard/list-article";
    }

}

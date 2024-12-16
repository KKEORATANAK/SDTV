package com.sdtvnews.sdtvnews.controller;

import com.sdtvnews.sdtvnews.config.CustomException;
import com.sdtvnews.sdtvnews.dto.request.ClientRequest;
import com.sdtvnews.sdtvnews.dto.response.ClientResponse;
import com.sdtvnews.sdtvnews.services.ClientService;
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
@RequestMapping("/client")
public class ClientController {

    @Autowired
    ClientService clientService;

    @GetMapping("/index")
    public String indexPage(Model model) {

        List<ClientResponse>lstResponsesData= clientService.getAllClient();
        // Retrieve the authorities of the logged-in user
        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        // Check for admin role
        boolean isAdmin = authorities.stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("lstResponsesData",lstResponsesData);
        return "dashboard/clients";  // Return login page
    }


    @PostMapping("/create")
    public String createClient(@ModelAttribute ClientRequest request, RedirectAttributes redirectAttributes) {
        try {
            // Attempt to create the client
            clientService.createClient(request);
            redirectAttributes.addFlashAttribute("create", "Client created successfully!");
        } catch (CustomException e) {
            // Handle the case where the client already exists or another error occurs
            redirectAttributes.addFlashAttribute("warning", e.getMessage());
        }
        return "redirect:/client/index"; // Redirect to the client index page
    }

    @GetMapping("/edit/{id}")
    @ResponseBody
    public ResponseEntity<ClientResponse> getClientById(@PathVariable String id) {
        try {
            Optional<ClientResponse> response = clientService.getClientById(Long.valueOf(id));
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
    public String updateClient(@ModelAttribute("client") ClientRequest client, RedirectAttributes redirectAttributes) {
        try {
            clientService.updateClient(Long.valueOf(client.getId()), client);
            redirectAttributes.addFlashAttribute("update", "Client update successfully!");
        } catch (CustomException e) {
            // Handle the case where the section already exists
            redirectAttributes.addFlashAttribute("warning", e.getMessage()); // Add warning message
        }
        return "redirect:/client/index"; // Redirect to the client page
    }

    @GetMapping("/activate/{id}")
    public String activateClient(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            // Call your service to activate the section
            clientService.updateClientStatus(Long.valueOf(id),"1");
            redirectAttributes.addFlashAttribute("active", "Client status activate updated successfully.");
        } catch (CustomException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to activate section: " + e.getMessage());
        }
        return "redirect:/client/index"; // Redirect to the client index page
    }

    @GetMapping("/dis-activate/{id}")
    public String DisActivateClient(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            // Call your service to activate the section
            clientService.updateClientStatus(Long.valueOf(id),"0");
            redirectAttributes.addFlashAttribute("active", "Client status dis-activate updated successfully.");
        } catch (CustomException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to activate section: " + e.getMessage());
        }
        return "redirect:/client/index"; // Redirect to the client index page
    }

    @GetMapping("/checkNameDuplicate")
    public ResponseEntity<Map<String, Boolean>> checkTitleDuplicate(@RequestParam("name") String name) {
        boolean isDuplicate = clientService.isNameDuplicate(name); // Call service to check duplication
        // Prepare response
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteArticle(@PathVariable Long id) {
        try {
            // Your delete logic here
            clientService.deleteClient(id);
            return ResponseEntity.ok("Client deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete client");
        }
    }

}

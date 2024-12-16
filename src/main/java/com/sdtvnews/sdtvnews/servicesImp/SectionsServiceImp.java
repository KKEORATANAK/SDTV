package com.sdtvnews.sdtvnews.servicesImp;

import com.sdtvnews.sdtvnews.config.CustomException;
import com.sdtvnews.sdtvnews.dto.request.SectionsRequest;
import com.sdtvnews.sdtvnews.dto.response.SectionsResponse;
import com.sdtvnews.sdtvnews.entity.Sections;
import com.sdtvnews.sdtvnews.repository.SectionsRepository;
import com.sdtvnews.sdtvnews.services.SectionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SectionsServiceImp implements SectionsService {

    private final SectionsRepository sectionsRepository;

    @Override
    public SectionsRequest createSections(SectionsRequest sectionsRequest) {
        // Check if the sections already exists
        List<Sections> existingCategories = sectionsRepository.findAll();

        for (Sections sections : existingCategories) {
            if (sections.getName().equalsIgnoreCase(sectionsRequest.getName())) {
                // Return a message if the sections already exists
                throw new CustomException("Sections with the name '" + sectionsRequest.getName() + "' already exists.");
            }
        }

        // Create a new Sections if it doesn't exist
        Sections newSections = new Sections();
        newSections.setName(sectionsRequest.getName());
        newSections.setDescription(sectionsRequest.getDescription());
        newSections.setStatus("1");//1=active;0=dis-active
        newSections.setCreateBy(sectionsRequest.getCreateBy());
        newSections.setCreateDate(LocalDateTime.now());
        newSections.setIndexShow(sectionsRequest.getIndexShow());
        // Save the new sections
        sectionsRepository.save(newSections);

        return sectionsRequest; // or convert and return the saved entity as needed
    }

    @Override
    public List<SectionsResponse> getAllSections() {
        List<Sections> categories = sectionsRepository.findAll(); // Retrieve all sections from the repository
        List<SectionsResponse> sectionsResponses = new ArrayList<>();

        for (Sections sections : categories) {
            SectionsResponse response = new SectionsResponse();
            response.setId(sections.getId());
            response.setName(sections.getName());
            response.setDescription(sections.getDescription());
            response.setStatus(sections.getStatus());
            response.setCreateDate(sections.getCreateDate());
            response.setIndexShow(sections.getIndexShow());
            // Set other necessary fields

            sectionsResponses.add(response);
        }

        return sectionsResponses;
    }

    @Override
    public Optional<SectionsResponse> getSectionsById(Long id) {
        // Retrieve the sections by its ID
        Optional<Sections> sectionsOptional = sectionsRepository.findById(id);
        // Check if the sections exists
        if (sectionsOptional.isPresent()) {
            // Convert Sections to SectionsResponse if it exists
            Sections sections = sectionsOptional.get();
            SectionsResponse sectionsResponse = new SectionsResponse();
            sectionsResponse.setId(sections.getId());
            sectionsResponse.setName(sections.getName());
            sectionsResponse.setDescription(sections.getDescription());
            sectionsResponse.setStatus(sections.getStatus());
            sectionsResponse.setCreateDate(sections.getCreateDate());
            sectionsResponse.setIndexShow(sections.getIndexShow());
            // Set other necessary fields
            return Optional.of(sectionsResponse);
        } else {
            // Return an empty Optional if the sections doesn't exist
            return Optional.empty();
        }
    }

    @Override
    public void updateSections(Long id, SectionsRequest sectionsRequest) {
        // Find the sections by its ID
        Optional<Sections> sectionsOptional = sectionsRepository.findById(id);

        if (sectionsOptional.isPresent()) {
            Sections sections = sectionsOptional.get();
            // Update the sections details
            sections.setName(sectionsRequest.getName());
            sections.setDescription(sectionsRequest.getDescription());
            sections.setStatus(sectionsRequest.getStatus());
            sections.setIndexShow(sectionsRequest.getIndexShow());
            sections.setUpdateBy(sectionsRequest.getUpdateBy());
            sections.setUpdateDate(LocalDateTime.now());

            // Save the updated sections
            sectionsRepository.save(sections);
        } else {
            throw new CustomException("Sections not found with ID: " + id);
        }
    }


    @Override
    public void updateSectionsStatus(Long id, String status) {
        Optional<Sections> sectionsOptional = sectionsRepository.findById(id);

        if (sectionsOptional.isPresent()) {
            Sections sections = sectionsOptional.get();
            sections.setStatus(status); // Update the status (e.g., "0" for inactive)
            sectionsRepository.save(sections); // Save the updated sections
        } else {
            throw new CustomException("Sections not found with ID: " + id);
        }
    }

}

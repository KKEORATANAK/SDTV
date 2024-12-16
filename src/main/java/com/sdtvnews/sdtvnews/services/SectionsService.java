package com.sdtvnews.sdtvnews.services;

import com.sdtvnews.sdtvnews.dto.request.SectionsRequest;
import com.sdtvnews.sdtvnews.dto.response.SectionsResponse;

import java.util.List;
import java.util.Optional;

public interface SectionsService {

    SectionsRequest createSections(SectionsRequest sectionsRequest);

    List<SectionsResponse>getAllSections();

    Optional<SectionsResponse>getSectionsById(Long id);

    void updateSections(Long id, SectionsRequest sectionsRequest);

    void updateSectionsStatus(Long id, String status);

}

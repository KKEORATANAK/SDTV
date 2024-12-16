package com.sdtvnews.sdtvnews.services;

import com.sdtvnews.sdtvnews.dto.request.AdsRequest;
import com.sdtvnews.sdtvnews.dto.response.AdsResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface AdsService {

    AdsRequest createAds(AdsRequest request)throws IOException;

    List<AdsResponse>getAllAds();

    Optional<AdsResponse>getAdsById(Long id);

    void updateAds(Long id, AdsRequest advertiseWithUsRequest, MultipartFile imageFile) throws IOException;

    void updateAdsStatus(Long id, String status);

    String saveImage(MultipartFile imageFile) throws IOException;
}

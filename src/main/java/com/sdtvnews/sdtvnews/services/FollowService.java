package com.sdtvnews.sdtvnews.services;

import com.sdtvnews.sdtvnews.dto.request.FollowRequest;
import com.sdtvnews.sdtvnews.dto.response.FollowResponse;

import java.util.List;
import java.util.Optional;

public interface FollowService {

    FollowRequest createFollow(FollowRequest followRequest);

    List<FollowResponse>getAllFollow();

    Optional<FollowResponse>getFollowById(Long id);

    void updateFollow(Long id, FollowRequest followRequest);

    void updateFollowStatus(Long id, String status);

}

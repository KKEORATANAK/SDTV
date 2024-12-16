package com.sdtvnews.sdtvnews.servicesImp;

import com.sdtvnews.sdtvnews.config.CustomException;
import com.sdtvnews.sdtvnews.dto.request.FollowRequest;
import com.sdtvnews.sdtvnews.dto.response.FollowResponse;
import com.sdtvnews.sdtvnews.entity.Follow;
import com.sdtvnews.sdtvnews.repository.FollowRepository;
import com.sdtvnews.sdtvnews.services.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowServiceImp implements FollowService {

    private final FollowRepository followRepository;

    @Override
    public FollowRequest createFollow(FollowRequest followRequest) {
        // Check if the follow already exists
        List<Follow> existingCategories = followRepository.findAll();

        for (Follow follow : existingCategories) {
            if (follow.getName().equalsIgnoreCase(followRequest.getName())) {
                // Return a message if the follow already exists
                throw new CustomException("Follow with the name '" + followRequest.getName() + "' already exists.");
            }
        }

        // Create a new Follow if it doesn't exist
        Follow newFollow = new Follow();
        newFollow.setName(followRequest.getName());
        newFollow.setDescription(followRequest.getDescription());
        newFollow.setStatus("1");//1=active;0=dis-active
        newFollow.setCreateBy(followRequest.getCreateBy());
        newFollow.setCreateDate(LocalDateTime.now());
        newFollow.setIndexShow(followRequest.getIndexShow());
        // Save the new follow
        followRepository.save(newFollow);

        return followRequest; // or convert and return the saved entity as needed
    }

    @Override
    public List<FollowResponse> getAllFollow() {
        List<Follow> categories = followRepository.findAll(); // Retrieve all follow from the repository
        List<FollowResponse> followResponses = new ArrayList<>();

        for (Follow follow : categories) {
            FollowResponse response = new FollowResponse();
            response.setId(follow.getId());
            response.setName(follow.getName());
            response.setDescription(follow.getDescription());
            response.setStatus(follow.getStatus());
            response.setCreateDate(follow.getCreateDate());
            response.setIndexShow(follow.getIndexShow());
            // Set other necessary fields

            followResponses.add(response);
        }

        return followResponses;
    }

    @Override
    public Optional<FollowResponse> getFollowById(Long id) {
        // Retrieve the follow by its ID
        Optional<Follow> followOptional = followRepository.findById(id);

        // Check if the follow exists
        if (followOptional.isPresent()) {
            // Convert Follow to FollowResponse if it exists
            Follow follow = followOptional.get();
            FollowResponse followResponse = new FollowResponse();
            followResponse.setId(follow.getId());
            followResponse.setName(follow.getName());
            followResponse.setDescription(follow.getDescription());
            followResponse.setStatus(follow.getStatus());
            followResponse.setCreateDate(follow.getCreateDate());
            followResponse.setIndexShow(follow.getIndexShow());
            // Set other necessary fields

            return Optional.of(followResponse);
        } else {
            // Return an empty Optional if the follow doesn't exist
            return Optional.empty();
        }
    }

    @Override
    public void updateFollow(Long id, FollowRequest followRequest) {
        // Find the follow by its ID
        Optional<Follow> followOptional = followRepository.findById(id);

        if (followOptional.isPresent()) {
            Follow follow = followOptional.get();
            // Update the follow details
            follow.setName(followRequest.getName());
            follow.setDescription(followRequest.getDescription());
            follow.setStatus(followRequest.getStatus());
            follow.setIndexShow(followRequest.getIndexShow());
            follow.setUpdateBy(followRequest.getUpdateBy());
            follow.setUpdateDate(LocalDateTime.now());

            // Save the updated follow
            followRepository.save(follow);
        } else {
            throw new CustomException("Follow not found with ID: " + id);
        }
    }


    @Override
    public void updateFollowStatus(Long id, String status) {
        Optional<Follow> followOptional = followRepository.findById(id);

        if (followOptional.isPresent()) {
            Follow follow = followOptional.get();
            follow.setStatus(status); // Update the status (e.g., "0" for inactive)
            followRepository.save(follow); // Save the updated follow
        } else {
            throw new CustomException("Follow not found with ID: " + id);
        }
    }

}

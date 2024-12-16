package com.sdtvnews.sdtvnews.services;

import com.sdtvnews.sdtvnews.dto.request.UserRequest;
import com.sdtvnews.sdtvnews.dto.response.UserResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserRequest createUser(UserRequest request);

    List<UserResponse>getAllUser();

    List<UserResponse> getActiveUser();

    Optional<UserResponse>getUserById(Long id);

    void updateUser(Long id, UserRequest categoryRequest);

    void updateUserStatus(Long id, String status);

    boolean isNameDuplicate(String name);

    void deleteUser(Long id);
}

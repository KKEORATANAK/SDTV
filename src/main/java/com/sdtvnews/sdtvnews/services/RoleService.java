package com.sdtvnews.sdtvnews.services;

import com.sdtvnews.sdtvnews.dto.request.RoleRequest;
import com.sdtvnews.sdtvnews.dto.response.RoleResponse;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    RoleRequest createRole(RoleRequest request);

    List<RoleResponse>getAllRole();

    List<RoleResponse>getActiveRole();

    Optional<RoleResponse>getRoleById(Long id);

    void updateRole(Long id, RoleRequest request);

    void updateRoleStatus(Long id, String status);

    boolean isNameDuplicate(String name);

}

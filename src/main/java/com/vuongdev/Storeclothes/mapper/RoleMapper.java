package com.vuongdev.Storeclothes.mapper;

import com.vuongdev.Storeclothes.dto.request.RoleRequest;
import com.vuongdev.Storeclothes.dto.response.RoleResponse;
import com.vuongdev.Storeclothes.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role mapToRole(RoleRequest request);
    RoleResponse mapToRoleResponse(Role role);

    void updateRole(RoleRequest request, @MappingTarget Role role);
}

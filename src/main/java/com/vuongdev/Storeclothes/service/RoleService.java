package com.vuongdev.Storeclothes.service;

import com.vuongdev.Storeclothes.dto.request.RoleRequest;
import com.vuongdev.Storeclothes.dto.response.RoleResponse;
import com.vuongdev.Storeclothes.entity.Role;
import com.vuongdev.Storeclothes.exception.AppException;
import com.vuongdev.Storeclothes.exception.ErrorCode;
import com.vuongdev.Storeclothes.mapper.RoleMapper;
import com.vuongdev.Storeclothes.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    public RoleResponse createRole(RoleRequest request){
        if(roleRepository.existsByName(request.getName())){
            throw new AppException(ErrorCode.ROLE_EXISTS);
        }

        Role role = roleMapper.mapToRole(request);
        return roleMapper.mapToRoleResponse(roleRepository.save(role));
    }

}

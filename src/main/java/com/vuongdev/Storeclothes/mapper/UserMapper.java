package com.vuongdev.Storeclothes.mapper;

import com.vuongdev.Storeclothes.dto.request.UserRequest;
import com.vuongdev.Storeclothes.dto.request.UserRequestUpdate;
import com.vuongdev.Storeclothes.dto.response.UserResponse;
import com.vuongdev.Storeclothes.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User mapToUser(UserRequest request);
    UserResponse mapToUserResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(UserRequestUpdate requestUpdate, @MappingTarget User user);
}

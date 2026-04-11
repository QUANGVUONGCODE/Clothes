package com.vuongdev.Storeclothes.service;

import com.vuongdev.Storeclothes.dto.request.UserRequest;
import com.vuongdev.Storeclothes.dto.request.UserRequestUpdate;
import com.vuongdev.Storeclothes.dto.response.UserResponse;
import com.vuongdev.Storeclothes.entity.Role;
import com.vuongdev.Storeclothes.entity.User;
import com.vuongdev.Storeclothes.enums.RolePlay;
import com.vuongdev.Storeclothes.exception.AppException;
import com.vuongdev.Storeclothes.exception.ErrorCode;
import com.vuongdev.Storeclothes.mapper.UserMapper;
import com.vuongdev.Storeclothes.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserRequest userRequest){
        if(userRepository.existsByPhoneNumber(userRequest.getPhoneNumber())){
            throw new AppException(ErrorCode.PHONE_NUMBER_EXISTS);
        }
        if(userRepository.existsByEmail(userRequest.getEmail())){
            throw new AppException(ErrorCode.EMAIL_EXISTS);
        }

        if(userRequest.getFacebookAccountId() == 0 && userRequest.getGoogleAccountId() ==0){
            String password = userRequest.getPassword();
            userRequest.setPassword(password);
        }
        log.info("Password: {}", userRequest.getPassword());
        log.info("Retype Password: {}", userRequest.getRetypePassword());
        if(!userRequest.getPassword().equals(userRequest.getRetypePassword())){
            throw new AppException(ErrorCode.RETYPE_PASSWORD_WRONG);
        }
        User user = userMapper.mapToUser(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        Role role = new Role();
        role.setId(2L);
        role.setName(RolePlay.USER.name());
        user.setRole(role);
        user.setActive(true);
        return userMapper.mapToUserResponse(userRepository.save(user));
    }

    public List<UserResponse> getAllUser(){
        return userRepository.findAll().stream()
                .map(userMapper::mapToUserResponse)
                .toList();
    }

    public UserResponse updateUser(UserRequestUpdate requestUpdate, Long id){
        User user = userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_USER_ID)
        );
        if(userRepository.existsByPhoneNumber(requestUpdate.getPhoneNumber())){
            throw new AppException(ErrorCode.PHONE_NUMBER_EXISTS);
        }
        if(userRepository.existsByEmail(requestUpdate.getEmail())){
            throw new AppException(ErrorCode.EMAIL_EXISTS);
        }

        userMapper.updateUser(requestUpdate, user);
        return userMapper.mapToUserResponse(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_USER_ID)
        );
        user.setActive(false);
        userRepository.save(user);
    }

    public UserResponse getMyInfor(){
        var context = SecurityContextHolder.getContext();
        Authentication auth = context == null ? null : context.getAuthentication();
        if(auth == null || !auth.isAuthenticated()){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String phoneNumber = auth.getName();
        if(phoneNumber == null || phoneNumber.isBlank()){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        log.info("ROLE: {}", auth.getAuthorities());
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTS)
        );
        return userMapper.mapToUserResponse(user);
    }
}
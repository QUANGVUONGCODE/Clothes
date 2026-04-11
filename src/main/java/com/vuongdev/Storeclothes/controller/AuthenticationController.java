package com.vuongdev.Storeclothes.controller;

import com.nimbusds.jose.JOSEException;
import com.vuongdev.Storeclothes.dto.request.requestService.AuthenticationRequest;
import com.vuongdev.Storeclothes.dto.request.requestService.IntrospectRequest;
import com.vuongdev.Storeclothes.dto.request.requestService.LogoutRequest;
import com.vuongdev.Storeclothes.dto.request.requestService.RefreshRequest;
import com.vuongdev.Storeclothes.dto.response.ApiResponse;
import com.vuongdev.Storeclothes.dto.response.responseService.AuthenticationResponse;
import com.vuongdev.Storeclothes.dto.response.responseService.IntrospectResponse;
import com.vuongdev.Storeclothes.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/log-in")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();

    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspectResponse(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refresh(@RequestBody RefreshRequest request) throws ParseException, JOSEException{
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<String> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException{
        authenticationService.logout(request);
        return ApiResponse.<String>builder()
                .result("Logout successfully")
                .build();
    }
}


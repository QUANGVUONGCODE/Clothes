package com.vuongdev.Storeclothes.controller;

import com.vuongdev.Storeclothes.dto.request.SizeRequest;
import com.vuongdev.Storeclothes.dto.response.ApiResponse;
import com.vuongdev.Storeclothes.dto.response.SizeResponse;
import com.vuongdev.Storeclothes.service.SizeService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/sizes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SizeController {
    SizeService sizeService;

    @PostMapping
    ApiResponse<SizeResponse> createSize(@RequestBody SizeRequest request){
        return ApiResponse.<SizeResponse>builder()
                .result(sizeService.createSize(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<SizeResponse>> getAllSizes(){
        return ApiResponse.<List<SizeResponse>>builder()
                .result(sizeService.getAllSizes())
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<SizeResponse> updateSize(@PathVariable Long id, @RequestBody SizeRequest request){
        return ApiResponse.<SizeResponse>builder()
                .result(sizeService.updateSize(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteSize(@PathVariable Long id){
        sizeService.deleteSize(id);
        return ApiResponse.<String>builder()
                .result("Size deleted successfully")
                .build();
    }
}

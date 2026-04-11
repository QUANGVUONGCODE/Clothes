package com.vuongdev.Storeclothes.controller;

import com.vuongdev.Storeclothes.dto.request.ColorRequest;
import com.vuongdev.Storeclothes.dto.response.ApiResponse;
import com.vuongdev.Storeclothes.dto.response.ColorResponse;
import com.vuongdev.Storeclothes.service.ColorService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/colors")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ColorController {
    ColorService colorService;

    @PostMapping
    ApiResponse<ColorResponse> createColor(@RequestBody ColorRequest request){
        return ApiResponse.<ColorResponse>builder()
                .result(colorService.createColor(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<ColorResponse>> getAllColor(){
        return ApiResponse.<List<ColorResponse>>builder()
                .result(colorService.getAllColor())
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<ColorResponse> updateColor(@PathVariable Long id, @RequestBody ColorRequest request){
        return ApiResponse.<ColorResponse>builder()
                .result(colorService.updateColor(id, request))
                .build();
    }


    @DeleteMapping("/{id}")
    ApiResponse<String> deleteColor(@PathVariable Long id){
        colorService.deleteColor(id);
        return ApiResponse.<String>builder()
                .result("Delete color successfully")
                .build();
    }
}

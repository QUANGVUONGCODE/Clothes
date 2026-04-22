package com.vuongdev.Storeclothes.controller;

import com.vuongdev.Storeclothes.dto.response.ApiResponse;
import com.vuongdev.Storeclothes.entity.SubCategoryImage;
import com.vuongdev.Storeclothes.service.SubCategoryImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("${api.prefix}/sub-category-images")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubCategoryImageController {
    SubCategoryImageService subCategoryImageService;
    @PostMapping("/upload")
    public ApiResponse<?> uploadSubCategoryImage(
            @RequestParam("subCategoryId") Long subCategoryId,
            @RequestPart("files") MultipartFile files
    ) throws IOException{
        try {
            return ApiResponse.<SubCategoryImage>builder()
                    .result(subCategoryImageService.uploadSubCategoryImage(subCategoryId, files))
                    .build();
        } catch (Exception e) {
            return ApiResponse.<String>builder()
                    .result(e.getMessage())
                    .build();
        }
    }
}

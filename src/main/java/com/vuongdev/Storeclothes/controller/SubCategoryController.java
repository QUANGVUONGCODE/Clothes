package com.vuongdev.Storeclothes.controller;

import com.vuongdev.Storeclothes.dto.request.SubCategoryRequest;
import com.vuongdev.Storeclothes.dto.response.ApiResponse;
import com.vuongdev.Storeclothes.dto.response.SubCategoryResponse;
import com.vuongdev.Storeclothes.service.SubCategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/sub-categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubCategoryController {
    SubCategoryService subCategoryService;

    @PostMapping
    ApiResponse<SubCategoryResponse> createSubCategory(@RequestBody SubCategoryRequest request){
        return ApiResponse.<SubCategoryResponse>builder()
                .result(subCategoryService.createSubCategory(request))
                .build();
    }

    @GetMapping("/{categoryId}")
    ApiResponse<List<SubCategoryResponse>> getAllSubCategoriesByCategoryId(@PathVariable Long categoryId){
        return ApiResponse.<List<SubCategoryResponse>>builder()
                .result(subCategoryService.getAllSubCategoriesByCategoryId(categoryId))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<SubCategoryResponse> updateSubCategory(@PathVariable Long id, @RequestBody SubCategoryRequest request){
        return ApiResponse.<SubCategoryResponse>builder()
                .result(subCategoryService.updateSubCategory(id, request))
                .build();
    }


    @DeleteMapping("/{id}")
    ApiResponse<String> deleteSubCategory(@PathVariable Long id){
        subCategoryService.deleteSubCategory(id);
        return ApiResponse.<String>builder()
                .result("Subcategory deleted successfully")
                .build();
    }

}

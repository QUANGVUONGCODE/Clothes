package com.vuongdev.Storeclothes.controller;

import com.vuongdev.Storeclothes.dto.request.CategoryRequest;
import com.vuongdev.Storeclothes.dto.response.ApiResponse;
import com.vuongdev.Storeclothes.dto.response.CategoryResponse;
import com.vuongdev.Storeclothes.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;

    @PostMapping
    ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryRequest request){
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.createCategory(request))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<List<CategoryResponse>> getAllCategoriesByDepartmentId(@PathVariable Long id){
        return ApiResponse.<List<CategoryResponse>>builder()
                .result(categoryService.getAllCategoriesByDepartmentId(id))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<CategoryResponse> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest request){
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.updateCategory(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ApiResponse.<String>builder()
                .result("Category deleted successfully")
                .build();
    }
}

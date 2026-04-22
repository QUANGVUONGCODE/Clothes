package com.vuongdev.Storeclothes.controller;

import com.vuongdev.Storeclothes.dto.request.SubCategoryRequest;
import com.vuongdev.Storeclothes.dto.response.ApiResponse;
import com.vuongdev.Storeclothes.dto.response.SubCategoryListResponse;
import com.vuongdev.Storeclothes.dto.response.SubCategoryResponse;
import com.vuongdev.Storeclothes.service.SubCategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @GetMapping("/search")
    ApiResponse<SubCategoryListResponse> getSubCategoriesBySearch(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "limit") int limit,
            @RequestParam(defaultValue = "", name = "department_id") Long departmentId,
            @RequestParam(defaultValue = "", name = "category_id") Long categoryId
    ){
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").ascending());


        Page<SubCategoryResponse> subCategoryResponsesPage = subCategoryService.searchSubCategories(pageRequest, categoryId, departmentId);
        List<SubCategoryResponse> subCategoryResponses = subCategoryResponsesPage.getContent();
        int totalPages = subCategoryResponsesPage.getNumber();
        return ApiResponse.<SubCategoryListResponse>builder()
                .result(SubCategoryListResponse.builder()
                        .subCategoryResponseList(subCategoryResponses)
                        .page(totalPages)
                        .build())
                .build();

    }
}

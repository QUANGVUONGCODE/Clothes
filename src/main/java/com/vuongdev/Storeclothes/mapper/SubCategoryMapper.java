package com.vuongdev.Storeclothes.mapper;

import com.vuongdev.Storeclothes.dto.request.CategoryRequest;
import com.vuongdev.Storeclothes.dto.request.SubCategoryRequest;
import com.vuongdev.Storeclothes.dto.response.CategoryResponse;
import com.vuongdev.Storeclothes.dto.response.SubCategoryResponse;
import com.vuongdev.Storeclothes.entity.Category;
import com.vuongdev.Storeclothes.entity.SubCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SubCategoryMapper {

    SubCategory mapToSubCategory(SubCategoryRequest request);

    SubCategoryResponse mapToSubCategoryResponse(SubCategory subCategory);

    void updateSubCategory(SubCategoryRequest request, @MappingTarget SubCategory subCategory);
}

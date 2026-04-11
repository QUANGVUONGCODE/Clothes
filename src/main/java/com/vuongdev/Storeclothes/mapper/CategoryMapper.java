package com.vuongdev.Storeclothes.mapper;

import com.vuongdev.Storeclothes.dto.request.CategoryRequest;
import com.vuongdev.Storeclothes.dto.response.CategoryResponse;
import com.vuongdev.Storeclothes.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", ignore = true)
    Category mapToCategory(CategoryRequest request);

    @Mapping(source = "department", target = "departments")
    CategoryResponse mapToCategoryResponse(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", ignore = true)
    void updateCategory(CategoryRequest request, @MappingTarget Category category);
}
package com.vuongdev.Storeclothes.service;

import com.vuongdev.Storeclothes.dto.request.CategoryRequest;
import com.vuongdev.Storeclothes.dto.response.CategoryResponse;
import com.vuongdev.Storeclothes.entity.Category;
import com.vuongdev.Storeclothes.entity.Departments;
import com.vuongdev.Storeclothes.exception.AppException;
import com.vuongdev.Storeclothes.exception.ErrorCode;
import com.vuongdev.Storeclothes.mapper.CategoryMapper;
import com.vuongdev.Storeclothes.repository.CategoryRepository;
import com.vuongdev.Storeclothes.repository.DepartmentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    CategoryRepository categoryRepository;
    DepartmentRepository departmentRepository;
    CategoryMapper categoryMapper;

    public CategoryResponse createCategory(CategoryRequest request){
        if(categoryRepository.existsByNameAndDepartmentId(request.getName(), request.getDepartmentId())){
            throw new AppException(ErrorCode.CATEGORY_EXISTS);
        }

        Departments departments = departmentRepository.findById(request.getDepartmentId()).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_DEPARTMENT)
        );

        Category category = categoryMapper.mapToCategory(request);
        category.setDepartment(departments);
        return categoryMapper.mapToCategoryResponse(categoryRepository.save(category));
    }

    public List<CategoryResponse> getAllCategories(){
        return categoryRepository.findAll().stream().map(categoryMapper::mapToCategoryResponse).toList();
    }

    public List<CategoryResponse> getAllCategoriesByDepartmentId(Long departmentId){
        if(!departmentRepository.existsById(departmentId)){
            throw new AppException(ErrorCode.INVALID_ID_DEPARTMENT);
        }
        return categoryRepository.findAllByDepartmentId(departmentId)
                .stream()
                .map(categoryMapper::mapToCategoryResponse)
                .toList();
    }



    public CategoryResponse getCategoryById(Long id){
        return categoryMapper.mapToCategoryResponse(categoryRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_CATEGORY)
        ));
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest request){
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_CATEGORY)
        );

        if(request != null && request.getDepartmentId() != null){
            Departments departments = departmentRepository.findById(request.getDepartmentId()).orElseThrow(
                    () -> new AppException(ErrorCode.INVALID_ID_DEPARTMENT)
            );
            category.setDepartment(departments);
        }
        categoryMapper.updateCategory(request, category);
        return categoryMapper.mapToCategoryResponse(categoryRepository.save(category));
    }

    public void deleteCategory(Long id){
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_CATEGORY)
        );
        categoryRepository.delete(category);
    }

}

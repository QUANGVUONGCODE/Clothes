package com.vuongdev.Storeclothes.service;

import com.vuongdev.Storeclothes.dto.request.CategoryRequest;
import com.vuongdev.Storeclothes.dto.request.SubCategoryImageRequest;
import com.vuongdev.Storeclothes.dto.request.SubCategoryRequest;
import com.vuongdev.Storeclothes.dto.response.CategoryResponse;
import com.vuongdev.Storeclothes.dto.response.SubCategoryResponse;
import com.vuongdev.Storeclothes.entity.Category;
import com.vuongdev.Storeclothes.entity.SubCategory;
import com.vuongdev.Storeclothes.entity.SubCategoryImage;
import com.vuongdev.Storeclothes.exception.AppException;
import com.vuongdev.Storeclothes.exception.ErrorCode;
import com.vuongdev.Storeclothes.mapper.SubCategoryMapper;
import com.vuongdev.Storeclothes.repository.CategoryRepository;
import com.vuongdev.Storeclothes.repository.SubCategoryImageRepository;
import com.vuongdev.Storeclothes.repository.SubCategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubCategoryService {
    SubCategoryRepository subCategoryRepository;
    SubCategoryMapper subCategoryMapper;
    CategoryRepository categoryRepository;
    SubCategoryImageRepository subCategoryImageRepository;

    public SubCategoryResponse createSubCategory(SubCategoryRequest request) {
        if(subCategoryRepository.existsByNameAndCategoryId(request.getName(), request.getCategoryId())){
            throw new AppException(ErrorCode.SUB_CATEGORY_EXISTS);
        }
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_CATEGORY)
        );

        SubCategory subCategory = subCategoryMapper.mapToSubCategory(request);
        subCategory.setCategory(category);
        return subCategoryMapper.mapToSubCategoryResponse(subCategoryRepository.save(subCategory));
    }


    public List<SubCategoryResponse> getAllSubCategories(){
        return subCategoryRepository.findAll().stream().map(subCategoryMapper::mapToSubCategoryResponse).toList();
    }

    public List<SubCategoryResponse> getAllSubCategoriesByCategoryId(Long categoryId){
        return subCategoryRepository.findAllByCategoryId(categoryId)
                .stream()
                .map(subCategoryMapper::mapToSubCategoryResponse)
                .toList();
    }


    public SubCategoryResponse updateSubCategory(Long id, SubCategoryRequest request){
        SubCategory subCategory = subCategoryRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_SUB_CATEGORY)
        );

        if(request != null && request.getCategoryId() != null){
            Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
                    () -> new AppException(ErrorCode.INVALID_ID_CATEGORY)
            );
            subCategory.setCategory(category);
        }

        subCategoryMapper.updateSubCategory(request, subCategory);
        return subCategoryMapper.mapToSubCategoryResponse(subCategoryRepository.save(subCategory));
    }

    public void deleteSubCategory(Long id){
        SubCategory subCategory = subCategoryRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_SUB_CATEGORY)
        );
        subCategoryRepository.delete(subCategory);
    }


    public SubCategoryImage createSubCategoryImage(Long subCategoryId, SubCategoryImageRequest request){
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId).orElseThrow(
                () ->  new AppException(ErrorCode.INVALID_ID_SUB_CATEGORY)
        );

        SubCategoryImage subCategoryImage = SubCategoryImage.builder()
                .subCategory(subCategory)
                .imageUrl(request.getImageUrl())
                .build();

        return subCategoryImageRepository.save(subCategoryImage);
    }

    public void updatethumbnailSubCategory(Long id, String thumbnail){
        SubCategory subCategory = subCategoryRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ID_SUB_CATEGORY)
        );

        subCategory.setThumbnail(thumbnail);
        subCategoryRepository.save(subCategory);
    }

    public Page<SubCategoryResponse> searchSubCategories(
            Pageable pageable,
            Long categoryId,
            Long departmentId
    ){
        return subCategoryRepository.searchSubCategories(pageable, categoryId, departmentId).map(subCategoryMapper::mapToSubCategoryResponse);
    }
}

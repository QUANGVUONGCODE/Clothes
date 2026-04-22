package com.vuongdev.Storeclothes.repository;

import com.vuongdev.Storeclothes.entity.ProductImage;
import com.vuongdev.Storeclothes.entity.SubCategoryImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubCategoryImageRepository extends JpaRepository<SubCategoryImage, Long> {
    Optional<SubCategoryImage> findByImageUrl(String imageUrl);
    SubCategoryImage findBySubCategoryId(Long subCategoryId);
}

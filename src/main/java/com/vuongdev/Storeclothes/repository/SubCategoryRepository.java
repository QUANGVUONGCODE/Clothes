package com.vuongdev.Storeclothes.repository;

import com.vuongdev.Storeclothes.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    boolean existsByNameAndCategoryId(String name, Long categoryId);
    List<SubCategory> findAllByCategoryId(Long categoryId);
}

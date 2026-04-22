package com.vuongdev.Storeclothes.repository;

import com.vuongdev.Storeclothes.entity.ProductVariant;
import com.vuongdev.Storeclothes.entity.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    boolean existsByNameAndCategoryId(String name, Long categoryId);
    List<SubCategory> findAllByCategoryId(Long categoryId);



    @Query("""
    SELECT sc
    FROM SubCategory sc
    WHERE (:categoryId IS NULL OR sc.category.id = :categoryId)
      AND (:departmentId IS NULL OR sc.category.department.id = :departmentId)
""")
    Page<SubCategory> searchSubCategories(Pageable pageable,
                                          @Param("categoryId") Long categoryId,
                                          @Param("departmentId") Long departmentId);
}

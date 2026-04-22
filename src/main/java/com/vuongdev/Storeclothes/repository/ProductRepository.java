package com.vuongdev.Storeclothes.repository;

import com.vuongdev.Storeclothes.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);

    @Query("""
        SELECT p
        FROM Product p
        WHERE p.subCategory.id = :subCategoryId
          AND p.status = 'ACTIVE'
    """)
    List<Product> findAllActiveBySubCategoryId(@Param("subCategoryId") Long subCategoryId);

    @Query("""
        SELECT p
        FROM Product p
        WHERE p.status = 'ACTIVE'
          AND (
                :keyword IS NULL
                OR :keyword = ''
                OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
              )
    """)
    Page<Product> searchActiveProducts(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("""
        SELECT p
        FROM Product p
        WHERE p.subCategory.category.id = :categoryId
          AND p.status = 'ACTIVE'
          AND (
                :keyword IS NULL
                OR :keyword = ''
                OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
              )
    """)
    Page<Product> searchProductsByCategoryId(
            @Param("categoryId") Long categoryId,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("""
        SELECT p
        FROM Product p
        WHERE p.subCategory.category.department.id = :departmentId
          AND p.status = 'ACTIVE'
          AND (
                :keyword IS NULL
                OR :keyword = ''
                OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
              )
    """)
    Page<Product> searchProductsByDepartmentId(
            @Param("departmentId") Long departmentId,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("""
        SELECT p
        FROM Product p
        WHERE p.subCategory.id = :subCategoryId
          AND p.status = 'ACTIVE'
          AND (
                :keyword IS NULL
                OR :keyword = ''
                OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
              )
    """)
    Page<Product> searchProductsBySubCategoryId(
            @Param("subCategoryId") Long subCategoryId,
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
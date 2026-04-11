package com.vuongdev.Storeclothes.repository;

import com.vuongdev.Storeclothes.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);

    List<Product> findAllBySubCategoryId(Long subCategoryId);
}

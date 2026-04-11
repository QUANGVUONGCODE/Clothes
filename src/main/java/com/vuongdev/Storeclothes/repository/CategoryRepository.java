package com.vuongdev.Storeclothes.repository;

import com.vuongdev.Storeclothes.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByNameAndDepartmentId(String name, Long departmentId);

    List<Category> findAllByDepartmentId(Long departmentId);
}

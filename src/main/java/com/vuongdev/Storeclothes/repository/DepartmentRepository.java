package com.vuongdev.Storeclothes.repository;

import com.vuongdev.Storeclothes.entity.Departments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Departments, Long> {
    boolean existsByName(String name);
}

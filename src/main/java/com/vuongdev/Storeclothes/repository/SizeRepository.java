package com.vuongdev.Storeclothes.repository;

import com.vuongdev.Storeclothes.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SizeRepository extends JpaRepository<Size, Long> {
    boolean existsByName(String name);
}

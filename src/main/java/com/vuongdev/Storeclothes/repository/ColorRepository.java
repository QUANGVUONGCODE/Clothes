package com.vuongdev.Storeclothes.repository;

import com.vuongdev.Storeclothes.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<Color, Long> {
    boolean existsByName(String name);
}

package com.vuongdev.Storeclothes.repository;

import com.vuongdev.Storeclothes.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(String name);
}

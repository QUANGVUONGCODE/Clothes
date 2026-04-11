package com.vuongdev.Storeclothes.repository;

import com.vuongdev.Storeclothes.entity.InvalidToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidTokenRepository extends JpaRepository<InvalidToken, String> {
}

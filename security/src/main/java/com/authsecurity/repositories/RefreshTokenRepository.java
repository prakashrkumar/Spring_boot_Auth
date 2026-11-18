package com.authsecurity.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.authsecurity.entities.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,UUID> {
    
}

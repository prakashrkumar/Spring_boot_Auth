package com.authsecurity.dtos;

public record LoginRequest(
    String email,
    String password
) {
    
}

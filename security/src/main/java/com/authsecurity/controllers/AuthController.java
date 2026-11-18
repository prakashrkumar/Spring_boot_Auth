package com.authsecurity.controllers;

import java.time.Instant;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authsecurity.dtos.LoginRequest;
import com.authsecurity.dtos.TokenResponse;
import com.authsecurity.dtos.UserDto;
import com.authsecurity.entities.RefreshToken;
import com.authsecurity.entities.User;
import com.authsecurity.repositories.RefreshTokenRepository;
import com.authsecurity.repositories.UserRepository;
import com.authsecurity.security.CookieService;
import com.authsecurity.security.JwtService;
import com.authsecurity.services.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ModelMapper mapper;
    private  final RefreshTokenRepository refreshTokenRepository;
    private  final CookieService cookieService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest,HttpServletResponse response) {
        // authenticate
        authenticate(loginRequest);

        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid Username or password"));

        if (!user.isEnable()) {
            throw new DisabledException("User is disabled");
        }
        String jti=UUID.randomUUID().toString();
        var refreshTokenOb=RefreshToken.builder()
                         .jti(jti)
                         .user(user)
                         .createdAt(Instant.now())
                         .expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
                         .revoked(false)
                         .build();


                         // save refresh token
                         refreshTokenRepository.save(refreshTokenOb);


        // generate token
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken=jwtService.generateRefreshToken(user,refreshTokenOb.getJti());
        // use cookie service to attech refresh token in cookie

        cookieService.attachRefreshTokenCookie(response, refreshToken, (int) jwtService.getRefreshTtlSeconds());
        cookieService.addNoStoreHeaders(response);
        TokenResponse tokenResponse = TokenResponse.of(
                accessToken,
                refreshToken,
                jwtService.getAccessTtlSeconds(),
                mapper.map(user, UserDto.class)
        );

        return ResponseEntity.ok(tokenResponse);
    }

    private Authentication authenticate(LoginRequest loginRequest) {
        try {
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
            );
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}

package com.authsecurity.security;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.authsecurity.entities.Provider;
import com.authsecurity.entities.RefreshToken;
import com.authsecurity.entities.User;
import com.authsecurity.repositories.RefreshTokenRepository;
import com.authsecurity.repositories.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final RefreshTokenRepository refreshTokenRepository;

    // Frontend redirect URL — set in application-dev.properties or .env
    @Value("${app.auth.frontend.success-redirect}")
    private String frontEndSuccessUrl;

    private static final Logger logger = LoggerFactory.getLogger(OAuth2SuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        logger.info("OAuth2 login success");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String registrationId = "unknown";
        if (authentication instanceof OAuth2AuthenticationToken token) {
            registrationId = token.getAuthorizedClientRegistrationId();
        }
        logger.info("registrationId: {}", registrationId);

        User user;
        switch (registrationId) {
            case "google" -> {
                String googleId = oAuth2User.getAttributes().getOrDefault("sub", "").toString();
                String email    = oAuth2User.getAttributes().getOrDefault("email", "").toString();
                String name     = oAuth2User.getAttributes().getOrDefault("name", "").toString();
                String picture  = oAuth2User.getAttributes().getOrDefault("picture", "").toString();

                User newUser = User.builder()
                        .email(email).name(name).image(picture)
                        .enable(true).providerId(googleId).provider(Provider.GOOGLE)
                        .build();
                user = userRepository.findByEmail(email).orElseGet(() -> userRepository.save(newUser));
            }
            case "github" -> {
                Object idAttr    = oAuth2User.getAttributes().get("id");
                String githubId  = idAttr != null ? idAttr.toString() : "";
                String name      = oAuth2User.getAttributes().getOrDefault("login", "").toString();
                String image     = oAuth2User.getAttributes().getOrDefault("avatar_url", "").toString();
                Object emailAttr = oAuth2User.getAttributes().get("email");
                String email     = (emailAttr != null && !emailAttr.toString().isBlank())
                        ? emailAttr.toString()
                        : githubId + "+github@users.noreply.github.com";

                User newUser = User.builder()
                        .email(email).name(name).image(image)
                        .enable(true).providerId(githubId).provider(Provider.GITHUB)
                        .build();
                user = userRepository.findByEmail(email).orElseGet(() -> userRepository.save(newUser));
            }
            default -> throw new RuntimeException("Unsupported provider: " + registrationId);
        }

        // Issue tokens
        String jti = UUID.randomUUID().toString();
        RefreshToken refreshTokenOb = RefreshToken.builder()
                .jti(jti).user(user).revoked(false)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
                .build();
        refreshTokenRepository.save(refreshTokenOb);

        String accessToken  = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user, refreshTokenOb.getJti());
        cookieService.attachRefreshTokenCookie(response, refreshToken, (int) jwtService.getRefreshTtlSeconds());

        // Redirect to frontend with accessToken as query param
        response.sendRedirect(frontEndSuccessUrl + "?accessToken=" + accessToken);
    }
}

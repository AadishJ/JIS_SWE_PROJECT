package com.jis.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.jis.dto.AuthUserResponse;
import com.jis.dto.LoginRequest;
import com.jis.entity.User;
import com.jis.repository.UserRepository;
import com.jis.security.AuthenticatedUser;
import com.jis.security.JwtAuthenticationFilter;
import com.jis.security.JwtService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${jwt.cookie-secure:false}")
    private boolean cookieSecure;

    @Value("${jwt.cookie-same-site:Lax}")
    private String cookieSameSite;

    @PostMapping("/login")
    public AuthUserResponse login(@RequestBody LoginRequest request,
            jakarta.servlet.http.HttpServletResponse response) {

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password is required");
        }

        User user;
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        } else if (request.getName() != null && !request.getName().isBlank()) {
            user = userRepository.findFirstByNameIgnoreCase(request.getName().trim())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId or name is required");
        }

        if (!passwordMatches(request.getPassword(), user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        String token = jwtService.generateToken(user);

        ResponseCookie cookie = ResponseCookie.from(JwtAuthenticationFilter.AUTH_COOKIE, token)
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .sameSite(cookieSameSite)
                .maxAge(jwtService.getExpirationSeconds())
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return new AuthUserResponse(user.getUserId(), user.getName(), user.getRole());
    }

    @GetMapping("/me")
    public AuthUserResponse me(@AuthenticationPrincipal AuthenticatedUser currentUser) {
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        User user = userRepository.findById(currentUser.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));

        return new AuthUserResponse(user.getUserId(), user.getName(), user.getRole());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(jakarta.servlet.http.HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(JwtAuthenticationFilter.AUTH_COOKIE, "")
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .sameSite(cookieSameSite)
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.noContent().build();
    }

    private boolean passwordMatches(String rawPassword, User user) {
        String storedPassword = user.getPassword();

        if (storedPassword == null || storedPassword.isBlank()) {
            return false;
        }

        if (passwordEncoder.matches(rawPassword, storedPassword)) {
            return true;
        }

        // Backward compatibility for existing plain-text records.
        if (storedPassword.equals(rawPassword)) {
            user.setPassword(passwordEncoder.encode(rawPassword));
            userRepository.save(user);
            return true;
        }

        return false;
    }
}

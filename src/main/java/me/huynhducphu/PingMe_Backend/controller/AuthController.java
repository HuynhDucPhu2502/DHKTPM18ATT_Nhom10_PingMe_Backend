package me.huynhducphu.PingMe_Backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.huynhducphu.PingMe_Backend.dto.request.auth.UserLoginRequestDto;
import me.huynhducphu.PingMe_Backend.dto.response.ApiResponse;
import me.huynhducphu.PingMe_Backend.dto.response.auth.DefaultAuthResponseDto;
import me.huynhducphu.PingMe_Backend.dto.response.auth.UserSessionResponseDto;
import me.huynhducphu.PingMe_Backend.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Admin 8/4/2025
 **/
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<DefaultAuthResponseDto>> loginLocal(
            @RequestBody @Valid UserLoginRequestDto userLoginRequestDto
    ) {
        var authResultWrapper = authService.loginLocal(userLoginRequestDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, authResultWrapper.getRefreshTokenCookie().toString())
                .body(new ApiResponse<>(authResultWrapper.getDefaultAuthResponseDto()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, authService.logout().toString())
                .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<DefaultAuthResponseDto>> refreshSession(
            @CookieValue(value = "refresh_token") String refreshToken
    ) {
        var authResultWrapper = authService.refreshSession(refreshToken);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, authResultWrapper.getRefreshTokenCookie().toString())
                .body(new ApiResponse<>(authResultWrapper.getDefaultAuthResponseDto()));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserSessionResponseDto>> getCurrentUserSession() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(authService.getCurrentUserSession()));
    }

}

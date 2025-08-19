package me.huynhducphu.PingMe_Backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.huynhducphu.PingMe_Backend.dto.request.auth.*;
import me.huynhducphu.PingMe_Backend.dto.response.ApiResponse;
import me.huynhducphu.PingMe_Backend.dto.response.auth.DefaultAuthResponse;
import me.huynhducphu.PingMe_Backend.dto.response.auth.SessionMetaResponse;
import me.huynhducphu.PingMe_Backend.dto.response.auth.UserInfoResponse;
import me.huynhducphu.PingMe_Backend.dto.response.auth.UserSessionResponse;
import me.huynhducphu.PingMe_Backend.service.account.AccountManagementService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Admin 8/4/2025
 **/
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AccountManagementService accountManagementService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserSessionResponse>> registerLocal(
            @RequestBody @Valid RegisterRequest registerRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(accountManagementService.register(registerRequest)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<DefaultAuthResponse>> loginLocal(
            @RequestBody @Valid LoginRequest loginRequest
    ) {
        var authResultWrapper = accountManagementService.login(loginRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, authResultWrapper.getRefreshTokenCookie().toString())
                .body(new ApiResponse<>(authResultWrapper.getDefaultAuthResponse()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(value = "refresh_token", required = false) String refreshToken
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, accountManagementService.logout(refreshToken).toString())
                .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<DefaultAuthResponse>> refreshSession(
            @CookieValue(value = "refresh_token") String refreshToken,
            @RequestBody SessionMetaRequest sessionMetaRequest
    ) {
        var authResultWrapper = accountManagementService.refreshSession(refreshToken, sessionMetaRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, authResultWrapper.getRefreshTokenCookie().toString())
                .body(new ApiResponse<>(authResultWrapper.getDefaultAuthResponse()));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserSessionResponse>> getCurrentUserSession() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(accountManagementService.getCurrentUserSession()));
    }

    @GetMapping("/me/info")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getCurrentUserInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(accountManagementService.getCurrentUserInfo()));
    }

    @GetMapping("/me/sessions")
    public ResponseEntity<ApiResponse<List<SessionMetaResponse>>> getCurrentUserSessions(
            @CookieValue(value = "refresh_token") String refreshToken
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(accountManagementService.getCurrentUserAllSessionMetas(refreshToken)));
    }

    @PostMapping("/me/password")
    public ResponseEntity<ApiResponse<UserSessionResponse>> updateCurrentUserPassword(
            @RequestBody @Valid ChangePasswordRequest changePasswordRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(accountManagementService.updateCurrentUserPassword(changePasswordRequest)));
    }

    @PostMapping("/me/profile")
    public ResponseEntity<ApiResponse<UserSessionResponse>> updateCurrentUserProfile(
            @RequestBody @Valid ChangeProfileRequest changeProfileRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(accountManagementService.updateCurrentUserProfile(changeProfileRequest)));
    }

    @PostMapping("/me/avatar")
    public ResponseEntity<ApiResponse<UserSessionResponse>> updateCurrentUserAvatar(
            @RequestParam("avatar") MultipartFile avatarFile
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(accountManagementService.updateCurrentUserAvatar(avatarFile)));
    }

    @DeleteMapping("/me/sessions/{sessionId}")
    public ResponseEntity<ApiResponse<Void>> deleteCurrentUserSessions(
            @PathVariable String sessionId
    ) {
        accountManagementService.deleteCurrentUserSession(sessionId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>());
    }

}

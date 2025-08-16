package me.huynhducphu.PingMe_Backend.service;

import me.huynhducphu.PingMe_Backend.dto.request.auth.*;
import me.huynhducphu.PingMe_Backend.dto.common.AuthResultWrapper;
import me.huynhducphu.PingMe_Backend.dto.response.auth.UserDetailResponse;
import me.huynhducphu.PingMe_Backend.dto.response.auth.UserSessionResponse;
import me.huynhducphu.PingMe_Backend.model.User;
import org.springframework.http.ResponseCookie;
import org.springframework.web.multipart.MultipartFile;

/**
 * Admin 8/4/2025
 **/
public interface AuthService {
    UserSessionResponse registerLocal(
            RegisterRequest registerRequest);

    AuthResultWrapper loginLocal(LoginRequest loginRequest);

    ResponseCookie logout();

    AuthResultWrapper refreshSession(String refreshToken, SessionMetaRequest sessionMetaRequest);

    User getCurrentUser();

    UserSessionResponse getCurrentUserSession();

    UserDetailResponse getCurrentUserDetail();

    UserSessionResponse updateCurrentUserPassword(
            ChangePasswordRequest changePasswordRequest
    );

    UserSessionResponse updateCurrentUserProfile(
            ChangeProfileRequest changeProfileRequest
    );

    UserSessionResponse updateCurrentUserAvatar(
            MultipartFile avatarFile
    );
}

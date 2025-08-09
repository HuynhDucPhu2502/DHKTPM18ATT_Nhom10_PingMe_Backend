package me.huynhducphu.PingMe_Backend.service;

import me.huynhducphu.PingMe_Backend.dto.request.auth.UserLoginLocalRequestDto;
import me.huynhducphu.PingMe_Backend.dto.request.auth.UserRegisterLocalRequestDto;
import me.huynhducphu.PingMe_Backend.dto.response.auth.AuthResultWrapper;
import me.huynhducphu.PingMe_Backend.dto.response.auth.UserSessionResponseDto;
import me.huynhducphu.PingMe_Backend.model.User;
import org.springframework.http.ResponseCookie;

/**
 * Admin 8/4/2025
 **/
public interface AuthService {
    UserSessionResponseDto registerLocal(
            UserRegisterLocalRequestDto userRegisterLocalRequestDto);

    AuthResultWrapper loginLocal(UserLoginLocalRequestDto userLoginLocalRequestDto);

    ResponseCookie logout();

    AuthResultWrapper refreshSession(String refreshToken);

    User getCurrentUser();

    UserSessionResponseDto getCurrentUserSession();
}

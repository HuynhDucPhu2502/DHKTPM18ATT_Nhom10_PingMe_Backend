package me.huynhducphu.PingMe_Backend.service;

import me.huynhducphu.PingMe_Backend.dto.request.auth.UserLoginRequestDto;
import me.huynhducphu.PingMe_Backend.dto.response.auth.AuthResultWrapper;
import me.huynhducphu.PingMe_Backend.model.User;

/**
 * Admin 8/4/2025
 **/
public interface AuthService {
    AuthResultWrapper loginLocal(UserLoginRequestDto userLoginRequestDto);

    AuthResultWrapper refreshSession(String refreshToken);

    User getCurrentUser();
}

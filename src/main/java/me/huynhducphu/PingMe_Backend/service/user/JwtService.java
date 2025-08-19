package me.huynhducphu.PingMe_Backend.service.user;

import me.huynhducphu.PingMe_Backend.model.user.User;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Admin 8/3/2025
 **/
public interface JwtService {
    String buildJwt(User user, Long expirationRate);

    Jwt decodeJwt(String token);
}

package me.huynhducphu.PingMe_Backend.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Admin 8/4/2025
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DefaultAuthResponseDto {

    private UserSessionResponseDto userSession;
    private String accessToken;

}

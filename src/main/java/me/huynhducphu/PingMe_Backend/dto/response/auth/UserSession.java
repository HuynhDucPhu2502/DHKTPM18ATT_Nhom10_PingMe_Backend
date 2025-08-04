package me.huynhducphu.PingMe_Backend.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Admin 8/3/2025
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserSession {

    private String email;
    private String name;
    private String avatarUrl;
    private String updatedAt;

}

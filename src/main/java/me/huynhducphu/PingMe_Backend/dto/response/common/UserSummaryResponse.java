package me.huynhducphu.PingMe_Backend.dto.response.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Admin 8/19/2025
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSummaryResponse {

    private Long email;
    private String name;
    private String avatarUrl;

}

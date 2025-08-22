package me.huynhducphu.PingMe_Backend.config.websocket.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

/**
 * Admin 8/11/2025
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserSocketPrincipal implements Principal {

    private Long id;
    private String email;
    private String name;

    @Override
    public String getName() {
        return String.valueOf(id);
    }

}

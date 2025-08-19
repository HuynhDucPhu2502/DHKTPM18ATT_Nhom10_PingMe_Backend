package me.huynhducphu.PingMe_Backend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import me.huynhducphu.PingMe_Backend.model.user.User;
import me.huynhducphu.PingMe_Backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Admin 8/19/2025
 **/
@Component
@RequiredArgsConstructor
public class CurrentUserProviderImpl implements me.huynhducphu.PingMe_Backend.service.CurrentUserProvider {
    private final UserRepository userRepository;

    @Override
    public User get() {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository
                .getUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng hiện tại"));
    }
}

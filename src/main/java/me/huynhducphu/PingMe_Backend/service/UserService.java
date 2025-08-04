package me.huynhducphu.PingMe_Backend.service;

import me.huynhducphu.PingMe_Backend.dto.request.user.CreateUserRequestDto;
import me.huynhducphu.PingMe_Backend.dto.response.user.DefaultUserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Admin 8/3/2025
 **/
public interface UserService {
    DefaultUserResponseDto saveUser(CreateUserRequestDto createUserRequestDto);

    Page<DefaultUserResponseDto> getAllUsers(Pageable pageable);

    DefaultUserResponseDto getUserById(Long id);
}

package me.huynhducphu.PingMe_Backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.huynhducphu.PingMe_Backend.dto.request.user.CreateUserRequestDto;
import me.huynhducphu.PingMe_Backend.dto.response.ApiResponse;
import me.huynhducphu.PingMe_Backend.dto.response.PageResponseDto;
import me.huynhducphu.PingMe_Backend.dto.response.user.DefaultUserResponseDto;
import me.huynhducphu.PingMe_Backend.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Admin 8/3/2025
 **/
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<DefaultUserResponseDto>> saveUser(
            @RequestBody @Valid CreateUserRequestDto createUserRequestDto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(userService.saveUser(createUserRequestDto)));
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<DefaultUserResponseDto>> getAllUsers(
            @PageableDefault(size = 5) Pageable pageable
    ) {
        Page<DefaultUserResponseDto> defaultUserResponseDtoPage =
                userService.getAllUsers(pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new PageResponseDto<>(defaultUserResponseDtoPage));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DefaultUserResponseDto>> getUserById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(userService.getUserById(id)));
    }


}

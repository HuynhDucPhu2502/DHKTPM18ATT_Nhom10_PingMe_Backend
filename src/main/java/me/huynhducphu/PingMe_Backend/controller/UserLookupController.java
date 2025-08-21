package me.huynhducphu.PingMe_Backend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.huynhducphu.PingMe_Backend.dto.request.user_lookup.DefaultUserLookupRequest;
import me.huynhducphu.PingMe_Backend.dto.response.ApiResponse;
import me.huynhducphu.PingMe_Backend.dto.response.common.UserSummaryResponse;
import me.huynhducphu.PingMe_Backend.service.user_lookup.UserLookupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin 8/19/2025
 **/
@Tag(
        name = "User Lookup",
        description = "Các endpoint tra cứu người dùng"
)
@RestController
@RequestMapping("/users/lookup")
@RequiredArgsConstructor
public class UserLookupController {

    private final UserLookupService userLookupService;

    @GetMapping
    public ResponseEntity<ApiResponse<UserSummaryResponse>> lookupUser(
            @RequestBody @Valid DefaultUserLookupRequest defaultUserLookupRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>(userLookupService.lookupUser(defaultUserLookupRequest)));
    }

}

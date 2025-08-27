package me.huynhducphu.PingMe_Backend.controller.chat;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.huynhducphu.PingMe_Backend.dto.request.chat.MarkReadRequest;
import me.huynhducphu.PingMe_Backend.dto.request.chat.SendMessageRequest;
import me.huynhducphu.PingMe_Backend.dto.response.ApiResponse;
import me.huynhducphu.PingMe_Backend.dto.response.chat.MessageResponse;
import me.huynhducphu.PingMe_Backend.dto.response.chat.ReadStateResponse;
import me.huynhducphu.PingMe_Backend.service.chat.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin 8/26/2025
 *
 **/
@Tag(
        name = "Messages",
        description = "Các endpoints xử lý tin nhắn"
)
@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<MessageResponse>> sendMessage(
            @RequestBody @Valid SendMessageRequest sendMessageRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(messageService.sendMessage(sendMessageRequest)));
    }

    @PostMapping("/read")
    public ResponseEntity<ApiResponse<ReadStateResponse>> sendMessage(
            @RequestBody @Valid MarkReadRequest markReadRequest
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(messageService.markAsRead(markReadRequest)));
    }


}

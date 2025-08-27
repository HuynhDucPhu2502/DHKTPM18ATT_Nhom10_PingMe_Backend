package me.huynhducphu.PingMe_Backend.dto.request.chat.message;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Admin 8/26/2025
 *
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MarkReadRequest {
    @NotNull(message = "Mã tin nhắn không được để trống")
    private Long lastReadMessageId;

    @NotNull(message = "Mã phòng không được để trống")
    private Long roomId;

}

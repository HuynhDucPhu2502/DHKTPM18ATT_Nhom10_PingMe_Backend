package me.huynhducphu.PingMe_Backend.service.chat;

import me.huynhducphu.PingMe_Backend.dto.request.chat.MarkReadRequest;
import me.huynhducphu.PingMe_Backend.dto.request.chat.SendMessageRequest;
import me.huynhducphu.PingMe_Backend.dto.response.chat.MessageResponse;
import me.huynhducphu.PingMe_Backend.dto.response.chat.ReadStateResponse;

/**
 * Admin 8/26/2025
 *
 **/
public interface MessageService {
    MessageResponse sendMessage(SendMessageRequest sendMessageRequest);

    ReadStateResponse markAsRead(MarkReadRequest markReadRequest);
}

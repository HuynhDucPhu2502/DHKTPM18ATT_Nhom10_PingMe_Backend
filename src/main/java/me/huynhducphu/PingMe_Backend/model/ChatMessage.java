package me.huynhducphu.PingMe_Backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Admin 8/10/2025
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatMessage {
    private String sender;
    private String content;
}

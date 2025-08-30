package me.huynhducphu.PingMe_Backend.service.chat.impl;

import lombok.RequiredArgsConstructor;
import me.huynhducphu.PingMe_Backend.dto.response.chat.room.RoomParticipantResponse;
import me.huynhducphu.PingMe_Backend.dto.ws.chat.MessageCreatedEvent;
import me.huynhducphu.PingMe_Backend.dto.ws.chat.RoomUpdatedEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

/**
 * Admin 8/29/2025
 *
 **/
@Component
@RequiredArgsConstructor
public class ChatEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onMessageCreated(MessageCreatedEvent event) {
        long roomId = event.getMessageResponse().getRoomId();
        String destination = "/topic/rooms/" + roomId + "/messages";

        messagingTemplate.convertAndSend(destination, event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onRoomUpdated(RoomUpdatedEvent event) {
        List<Long> participantIds = event
                .getRoomResponse()
                .getParticipants()
                .stream()
                .map(RoomParticipantResponse::getUserId)
                .toList();

        for (Long userId : participantIds) {
            messagingTemplate.convertAndSendToUser(
                    userId.toString(),
                    "/queue/rooms",
                    event
            );
        }
    }
}

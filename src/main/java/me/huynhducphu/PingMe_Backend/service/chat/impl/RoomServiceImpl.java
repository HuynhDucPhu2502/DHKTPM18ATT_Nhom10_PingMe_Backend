package me.huynhducphu.PingMe_Backend.service.chat.impl;

import lombok.RequiredArgsConstructor;
import me.huynhducphu.PingMe_Backend.dto.request.chat.CreateOrGetDirectRoomRequest;
import me.huynhducphu.PingMe_Backend.dto.response.chat.RoomParticipantResponse;
import me.huynhducphu.PingMe_Backend.dto.response.chat.RoomResponse;
import me.huynhducphu.PingMe_Backend.model.Message;
import me.huynhducphu.PingMe_Backend.model.Room;
import me.huynhducphu.PingMe_Backend.model.RoomParticipant;
import me.huynhducphu.PingMe_Backend.model.common.RoomMemberId;
import me.huynhducphu.PingMe_Backend.model.constant.RoomRole;
import me.huynhducphu.PingMe_Backend.model.constant.RoomType;
import me.huynhducphu.PingMe_Backend.repository.RoomParticipantRepository;
import me.huynhducphu.PingMe_Backend.repository.RoomRepository;
import me.huynhducphu.PingMe_Backend.repository.UserRepository;
import me.huynhducphu.PingMe_Backend.service.common.CurrentUserProvider;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Admin 8/25/2025
 *
 **/
@Service
@Transactional
@RequiredArgsConstructor
public class RoomServiceImpl implements me.huynhducphu.PingMe_Backend.service.chat.RoomService {

    private final CurrentUserProvider currentUserProvider;

    private final RoomRepository roomRepository;
    private final RoomParticipantRepository roomParticipantRepository;
    private final UserRepository userRepository;

    @Override
    public RoomResponse createOrGetDirectRoom(CreateOrGetDirectRoomRequest createOrGetDirectRoomRequest) {
        var currentUser = currentUserProvider.get();

        if (currentUser.getId().equals(createOrGetDirectRoomRequest.getTargetUserId()))
            throw new IllegalArgumentException("Bạn không thể tự nhắn tin cho chính mình");

        if (!userRepository.existsById(createOrGetDirectRoomRequest.getTargetUserId()))
            throw new IllegalArgumentException("Người dùng cần nhắn tin không tồn tại");

        String directKey = buildDirectKey(currentUser.getId(), createOrGetDirectRoomRequest.getTargetUserId());

        var room = roomRepository.findByDirectKey(directKey).orElse(null);

        if (room != null) {
            ensureParticipants(room, currentUser.getId(), createOrGetDirectRoomRequest.getTargetUserId());
            return toResponse(room, roomParticipantRepository.findByRoom_Id(room.getId()));
        }

        try {
            var newRoom = new Room();

            newRoom.setRoomType(RoomType.DIRECT);
            newRoom.setDirectKey(directKey);
            newRoom.setName(null);
            newRoom.setLastMessage(null);
            newRoom.setLastMessageAt(null);

            var savedRoom = roomRepository.save(newRoom);

            addParticipant(savedRoom, currentUser.getId());
            addParticipant(savedRoom, createOrGetDirectRoomRequest.getTargetUserId());

            return toResponse(savedRoom, roomParticipantRepository.findByRoom_Id(savedRoom.getId()));
        } catch (DataIntegrityViolationException ex) {
            Room existed = roomRepository.findByDirectKey(directKey).orElseThrow(() -> ex);
            ensureParticipants(existed, currentUser.getId(), createOrGetDirectRoomRequest.getTargetUserId());
            return toResponse(existed, roomParticipantRepository.findByRoom_Id(existed.getId()));
        }
    }


    // =====================================
    // Utilities methods
    // =====================================
    private String buildDirectKey(Long a, Long b) {
        long low = Math.min(a, b);
        long high = Math.max(a, b);
        return low + "_" + high;
    }

    private void ensureParticipants(Room room, Long currentUserId, Long targetUserId) {
        addParticipant(room, currentUserId);
        addParticipant(room, targetUserId);
    }

    private void addParticipant(Room room, Long userId) {
        RoomMemberId pk = new RoomMemberId(room.getId(), userId);
        if (roomParticipantRepository.existsById(pk)) return;

        RoomParticipant rp = new RoomParticipant();
        rp.setId(pk);
        rp.setRoom(room);
        rp.setUser(userRepository.getReferenceById(userId));
        rp.setRole(RoomRole.MEMBER);

        try {
            roomParticipantRepository.save(rp);
        } catch (DataIntegrityViolationException ignored) {

        }
    }

    private RoomResponse toResponse(Room room, List<RoomParticipant> members) {
        List<RoomParticipantResponse> roomParticipantResponses = members
                .stream()
                .map(rp -> new RoomParticipantResponse(
                        rp.getUser().getId(),
                        rp.getRole(),
                        rp.getLastReadMessageId(),
                        rp.getLastReadAt()
                ))
                .toList();

        RoomResponse.LastMessage lastMessage = null;
        if (room.getLastMessage() != null) {
            Message message = room.getLastMessage();
            lastMessage = new RoomResponse.LastMessage(
                    message.getId(),
                    message.getSender().getId(),
                    message.getContent(),
                    message.getCreatedAt()
            );
        }

        RoomResponse res = new RoomResponse();
        res.setRoomId(room.getId());
        res.setRoomType(room.getRoomType());
        res.setDirectKey(room.getDirectKey());
        res.setName(room.getName());
        res.setLastMessage(lastMessage);
        res.setParticipants(roomParticipantResponses);
        return res;
    }

}

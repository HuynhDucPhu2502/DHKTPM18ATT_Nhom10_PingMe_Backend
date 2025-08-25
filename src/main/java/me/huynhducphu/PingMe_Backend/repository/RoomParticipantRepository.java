package me.huynhducphu.PingMe_Backend.repository;

import me.huynhducphu.PingMe_Backend.model.RoomParticipant;
import me.huynhducphu.PingMe_Backend.model.common.RoomMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Admin 8/25/2025
 *
 **/
@Repository
public interface RoomParticipantRepository extends JpaRepository<RoomParticipant, RoomMemberId> {
    List<RoomParticipant> findByRoomId(Long roomId);

    boolean existsByRoom_IdAndUser_Id(Long roomId, Long userId);
}

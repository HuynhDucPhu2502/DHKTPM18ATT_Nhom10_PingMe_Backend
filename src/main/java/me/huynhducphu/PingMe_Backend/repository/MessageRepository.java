package me.huynhducphu.PingMe_Backend.repository;

import me.huynhducphu.PingMe_Backend.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Admin 8/25/2025
 *
 **/
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findByRoom_IdOrderByIdDesc(Long roomId, Pageable pageable);

    Optional<Message> findByRoom_IdAndSender_IdAndClientMsgId(Long roomId, Long senderId, UUID clientMsgId);

    Optional<Message> findTopByRoom_IdOrderByIdDesc(Long roomId);

}

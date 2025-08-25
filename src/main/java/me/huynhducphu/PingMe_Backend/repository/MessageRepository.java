package me.huynhducphu.PingMe_Backend.repository;

import me.huynhducphu.PingMe_Backend.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Admin 8/25/2025
 *
 **/
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}

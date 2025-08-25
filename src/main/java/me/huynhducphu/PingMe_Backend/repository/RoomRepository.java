package me.huynhducphu.PingMe_Backend.repository;

import me.huynhducphu.PingMe_Backend.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Admin 8/25/2025
 *
 **/
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByDirectKey(String directKey);
}

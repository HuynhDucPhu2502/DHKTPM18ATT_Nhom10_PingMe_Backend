package me.huynhducphu.PingMe_Backend.repository;

import me.huynhducphu.PingMe_Backend.model.Message;
import me.huynhducphu.PingMe_Backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Admin 10/25/2025
 *
 **/
public interface RoleRepository extends JpaRepository<Role, Long> {
}

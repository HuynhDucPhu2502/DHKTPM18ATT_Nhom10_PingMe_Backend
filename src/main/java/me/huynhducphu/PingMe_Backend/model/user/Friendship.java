package me.huynhducphu.PingMe_Backend.model.user;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import me.huynhducphu.PingMe_Backend.model.common.BaseEntity;
import me.huynhducphu.PingMe_Backend.model.constant.FriendshipStatus;

/**
 * Admin 8/19/2025
 **/

public class Friendship extends BaseEntity {

    private Long id;

    private User userA;

    private User userB;

    private FriendshipStatus friendshipStatus;

    private Long userLowId;

    private Long userHighId;

    @PrePersist
    @PreUpdate
    private void normalizePair() {
        if (userA == null || userB == null) return;
        long a = userA.getId();
        long b = userB.getId();
        if (a <= b) {
            userLowId = a;
            userHighId = b;
        } else {
            userLowId = b;
            userHighId = a;
        }
    }
}

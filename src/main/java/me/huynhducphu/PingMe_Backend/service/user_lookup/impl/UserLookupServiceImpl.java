package me.huynhducphu.PingMe_Backend.service.user_lookup.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import me.huynhducphu.PingMe_Backend.dto.response.common.UserSummaryResponse;
import me.huynhducphu.PingMe_Backend.repository.FriendshipRepository;
import me.huynhducphu.PingMe_Backend.repository.UserRepository;
import me.huynhducphu.PingMe_Backend.service.common.CurrentUserProvider;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * Admin 8/21/2025
 **/
@Service
@RequiredArgsConstructor
public class UserLookupServiceImpl implements me.huynhducphu.PingMe_Backend.service.user_lookup.UserLookupService {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    private final CurrentUserProvider currentUserProvider;

    private final ModelMapper modelMapper;

    @Override
    public UserSummaryResponse lookupUser(String email) {
        // Lấy thộng tin người dùng hiện tại
        var currentUser = currentUserProvider.get();

        // Tìm người dùng theo email được truyền vào
        var targetUser = userRepository
                .getUserByEmail(email)
                .orElse(null);

        // Không tìm thấy người dùng theo email
        if (targetUser == null) return null;

        // Nếu email trỏ tới chính mình thì không cho phép
        if (targetUser.getId().equals(currentUser.getId()))
            throw new EntityNotFoundException("Bạn không thể tìm chính mình");

        // Chuyển entity sang DTO trả về
        var userSummaryResponse = modelMapper.map(targetUser, UserSummaryResponse.class);

        // ===================================================================================================
        // Kèm theo thông tin quan hệ bạn bè (Friendship) giữa currentUser và targetUser nếu có:

        // Tạo cặp (lowId, highId) theo quy tắc userId nhỏ hơn đứng trước để tra cứu
        Long lowId = Math.min(currentUser.getId(), targetUser.getId());
        Long highId = Math.max(currentUser.getId(), targetUser.getId());
        var friendship = friendshipRepository
                .findByUserLowIdAndUserHighId(lowId, highId)
                .orElse(null);

        // Nếu tồn tại quan hệ, gắn tóm tắt vào DTO; ngược lại set null
        if (friendship != null) {
            var friendshipSummary = new UserSummaryResponse.FriendshipSummary(
                    friendship.getId(),
                    friendship.getFriendshipStatus()
            );
            userSummaryResponse.setFriendshipSummary(friendshipSummary);
        } else userSummaryResponse.setFriendshipSummary(null);
        // ===================================================================================================

        return userSummaryResponse;
    }
}

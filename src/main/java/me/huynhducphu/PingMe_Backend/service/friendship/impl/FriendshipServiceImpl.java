package me.huynhducphu.PingMe_Backend.service.friendship.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import me.huynhducphu.PingMe_Backend.dto.request.friendship.FriendInvitationRequest;
import me.huynhducphu.PingMe_Backend.dto.response.common.UserSummaryResponse;
import me.huynhducphu.PingMe_Backend.model.constant.FriendshipStatus;
import me.huynhducphu.PingMe_Backend.model.user.Friendship;
import me.huynhducphu.PingMe_Backend.repository.FriendshipRepository;
import me.huynhducphu.PingMe_Backend.repository.UserRepository;
import me.huynhducphu.PingMe_Backend.service.common.CurrentUserProvider;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Admin 8/19/2025
 **/
@Service
@Transactional
@RequiredArgsConstructor
public class FriendshipServiceImpl implements me.huynhducphu.PingMe_Backend.service.friendship.FriendshipService {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    private final CurrentUserProvider currentUserProvider;

    private final ModelMapper modelMapper;

    @Override
    public void sendInvitation(FriendInvitationRequest friendInvitationRequest) {
        var currentUser = currentUserProvider.get();

        if (currentUser.getId().equals(friendInvitationRequest.getTargetUserId()))
            throw new EntityNotFoundException("Không thể kết bạn với chính mình");

        var targetUser = userRepository
                .findById(friendInvitationRequest.getTargetUserId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng cần kết bạn"));

        Long lowId = Math.min(currentUser.getId(), targetUser.getId());
        Long highId = Math.max(currentUser.getId(), targetUser.getId());

        if (friendshipRepository.existsByUserLowIdAndUserHighId(lowId, highId))
            throw new DataIntegrityViolationException("Lời kết bạn đã tồn tại");

        var friendship = new Friendship();
        friendship.setUserA(currentUser);
        friendship.setUserB(targetUser);
        friendship.setFriendshipStatus(FriendshipStatus.PENDING);
        friendship.setUserLowId(lowId);
        friendship.setUserHighId(highId);

        friendshipRepository.save(friendship);
    }

    @Override
    public void acceptInvitation(Long friendRequestId) {
        var current = currentUserProvider.get();

        var friendship = friendshipRepository
                .findById(friendRequestId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy lời mời kết bạn này"));

        if (friendship.getFriendshipStatus() != FriendshipStatus.PENDING)
            throw new DataIntegrityViolationException("Trạng thái lời mời không thích hợp");

        if (!friendship.getUserB().getId().equals(current.getId()))
            throw new DataIntegrityViolationException("Chỉ có người được nhận lời mời mới có thể chấp nhận");

        friendship.setFriendshipStatus(FriendshipStatus.ACCEPTED);
    }

    @Override
    public void rejectInvitation(Long friendRequestId) {
        var current = currentUserProvider.get();

        var friendship = friendshipRepository
                .findById(friendRequestId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy lời mời kết bạn này"));

        if (friendship.getFriendshipStatus() != FriendshipStatus.PENDING)
            throw new DataIntegrityViolationException("Trạng thái lời mời không thích hợp");

        var isParticipant = friendship.getUserA().getId().equals(current.getId())
                || friendship.getUserB().getId().equals(current.getId());
        if (!isParticipant)
            throw new DataIntegrityViolationException("Chỉ có người được nhận/gửi lời mời mới có thể hủy");

        friendshipRepository.delete(friendship);
    }

    @Override
    public Page<UserSummaryResponse> getAcceptedFriendshipList(Pageable pageable) {
        var currentUser = currentUserProvider.get();

        return friendshipRepository
                .findAllByFriendshipStatusAndUserA_IdOrFriendshipStatusAndUserB_Id(
                        FriendshipStatus.ACCEPTED, currentUser.getId(),
                        FriendshipStatus.ACCEPTED, currentUser.getId(),
                        pageable
                )
                .map(friendship -> {
                    var friend = friendship.getUserA().getId().equals(currentUser.getId())
                            ? friendship.getUserB()
                            : friendship.getUserA();
                    return modelMapper.map(friend, UserSummaryResponse.class);
                });
    }

    @Override
    public Page<UserSummaryResponse> getReceivedInvitations(Pageable pageable) {
        var currentUser = currentUserProvider.get();

        return friendshipRepository
                .findByFriendshipStatusAndUserB_Id(
                        FriendshipStatus.PENDING, currentUser.getId(), pageable
                )
                .map(friendship -> {
                    var inviter = friendship.getUserA();
                    return modelMapper.map(inviter, UserSummaryResponse.class);
                });
    }

    @Override
    public Page<UserSummaryResponse> getSentInvitations(Pageable pageable) {
        var currentUser = currentUserProvider.get();

        return friendshipRepository
                .findByFriendshipStatusAndUserA_Id(
                        FriendshipStatus.PENDING, currentUser.getId(), pageable
                )
                .map(friendship -> {
                    var invitee = friendship.getUserB();
                    return modelMapper.map(invitee, UserSummaryResponse.class);
                });
    }


}

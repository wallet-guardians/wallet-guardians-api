package com.walletguardians.walletguardiansapi.domain.friend.service;

import com.walletguardians.walletguardiansapi.domain.friend.controller.dto.FriendshipStatusDTO;
import com.walletguardians.walletguardiansapi.domain.friend.entity.FriendshipStatus;
import com.walletguardians.walletguardiansapi.domain.friend.repository.FriendshipStatusRepository;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final FriendshipStatusRepository friendshipStatusRepository;

    @Transactional
    public FriendshipStatusDTO sendFriendRequest(User sender, User receiver) {
        if (friendshipStatusRepository.findBySenderAndReceiver(sender, receiver).isPresent()) {
            throw new IllegalArgumentException("이미 친구 요청을 보냈습니다.");
        }

        FriendshipStatus friendshipStatus = FriendshipStatus.builder()
            .sender(sender)
            .receiver(receiver)
            .friendshipStatus(false)
            .build();

        FriendshipStatus savedStatus = friendshipStatusRepository.save(friendshipStatus);
        return new FriendshipStatusDTO(savedStatus);
    }

    @Transactional
    public boolean acceptFriendRequest(String receiverEmail, String senderEmail) {
        Optional<FriendshipStatus> friendshipStatusOpt = friendshipStatusRepository.findBySenderAndReceiverEmail(senderEmail, receiverEmail);

        if (friendshipStatusOpt.isEmpty()) {
            return false;
        }

        FriendshipStatus friendshipStatus = friendshipStatusOpt.get();

        friendshipStatus.updateFriendshipStatus(true);
        friendshipStatusRepository.save(friendshipStatus);

        return true;
    }

    @Transactional(readOnly = true)
    public List<FriendshipStatusDTO> getFollowingList(User user) {
        return friendshipStatusRepository.findBySender(user)
            .stream()
            .distinct()
            .map(FriendshipStatusDTO::fromEntity)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<FriendshipStatusDTO> getFollowerList(User user) {
        return friendshipStatusRepository.findByReceiver(user)
            .stream()
            .map(FriendshipStatusDTO::fromEntity)
            .toList();
    }
}

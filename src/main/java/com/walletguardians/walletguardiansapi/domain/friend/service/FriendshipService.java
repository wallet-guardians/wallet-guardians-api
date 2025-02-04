package com.walletguardians.walletguardiansapi.domain.friend.service;

import com.walletguardians.walletguardiansapi.domain.friend.controller.dto.FriendshipStatusDTO;
import com.walletguardians.walletguardiansapi.domain.friend.entity.FriendshipStatus;
import com.walletguardians.walletguardiansapi.domain.friend.entity.FriendshipStatusEnum;
import com.walletguardians.walletguardiansapi.domain.friend.repository.FriendshipStatusRepository;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

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
            .friendshipStatus(FriendshipStatusEnum.PENDING)
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
        friendshipStatus.updateFriendshipStatus(FriendshipStatusEnum.ACCEPTED);
        friendshipStatusRepository.save(friendshipStatus);

        return true;
    }

    @Transactional
    public boolean rejectFriendRequest(String receiverEmail, String senderEmail) {
        Optional<FriendshipStatus> friendshipStatusOpt = friendshipStatusRepository.findBySenderAndReceiverEmail(senderEmail, receiverEmail);

        if (friendshipStatusOpt.isEmpty()) {
            return false;
        }

        FriendshipStatus friendshipStatus = friendshipStatusOpt.get();
        friendshipStatusRepository.delete(friendshipStatus); // 요청을 삭제

        return true;
    }

    @Transactional
    public boolean deleteFriendship(String userEmail, String targetEmail) {
        Optional<FriendshipStatus> friendship = friendshipStatusRepository.findBySenderAndReceiverEmail(userEmail, targetEmail);

        if (friendship.isPresent()) {
            friendshipStatusRepository.delete(friendship.get());
            return true;
        }

        // 반대 방향의 친구 관계 확인 쌍방 삭제 가능
        Optional<FriendshipStatus> reverseFriendship = friendshipStatusRepository.findBySenderAndReceiverEmail(targetEmail, userEmail);
        if (reverseFriendship.isPresent()) {
            friendshipStatusRepository.delete(reverseFriendship.get());
            return true;
        }

        return false;
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

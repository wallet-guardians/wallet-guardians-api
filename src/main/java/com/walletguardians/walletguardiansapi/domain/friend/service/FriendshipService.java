package com.walletguardians.walletguardiansapi.domain.friend.service;

import com.walletguardians.walletguardiansapi.domain.friend.controller.dto.FriendshipStatusDTO;
import com.walletguardians.walletguardiansapi.domain.friend.entity.FriendshipStatus;
import com.walletguardians.walletguardiansapi.domain.friend.entity.FriendshipStatusEnum;
import com.walletguardians.walletguardiansapi.domain.friend.repository.FriendshipStatusRepository;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import com.walletguardians.walletguardiansapi.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final FriendshipStatusRepository friendshipStatusRepository;
    private final UserService userService;

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

        friendshipStatusRepository.save(friendshipStatus);
        return convertToDTO(friendshipStatus);
    }

    @Transactional
    public boolean acceptFriendRequest(String receiverEmail, String senderEmail) {
        return friendshipStatusRepository.findBySenderAndReceiverEmail(senderEmail, receiverEmail)
            .map(friendship -> {
                friendship.updateFriendshipStatus(FriendshipStatusEnum.ACCEPTED);
                friendshipStatusRepository.save(friendship);
                return true;
            })
            .orElse(false);
    }

    @Transactional
    public boolean rejectFriendRequest(String receiverEmail, String senderEmail) {
        return friendshipStatusRepository.findBySenderAndReceiverEmail(senderEmail, receiverEmail)
            .map(friendship -> {
                friendshipStatusRepository.delete(friendship);
                return true;
            })
            .orElse(false);
    }

    @Transactional
    public boolean deleteFriendship(String userEmail, String targetEmail) {
        Optional<FriendshipStatus> friendship = friendshipStatusRepository.findBySenderAndReceiverEmail(userEmail, targetEmail);

        if (friendship.isPresent()) {
            friendshipStatusRepository.delete(friendship.get());
            return true;
        }

        Optional<FriendshipStatus> reverseFriendship = friendshipStatusRepository.findBySenderAndReceiverEmail(targetEmail, userEmail);
        if (reverseFriendship.isPresent()) {
            friendshipStatusRepository.delete(reverseFriendship.get());
            return true;
        }

        return false;
    }

    @Transactional(readOnly = true)
    public List<FriendshipStatusDTO> getAcceptedFriends(String userEmail) {
        return friendshipStatusRepository.findAcceptedFriends(userEmail, FriendshipStatusEnum.ACCEPTED)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
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
    public List<FriendshipStatusDTO> getPendingRequests(String userEmail) {
        return friendshipStatusRepository.findPendingRequestsBySender(userEmail, FriendshipStatusEnum.PENDING)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FriendshipStatusDTO> getReceivedRequests(String userEmail) {
        return friendshipStatusRepository.findPendingRequestsByReceiver(userEmail, FriendshipStatusEnum.PENDING)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public boolean cancelFriendRequest(String senderEmail, String receiverEmail) {
        return friendshipStatusRepository.findBySenderAndReceiverEmail(senderEmail, receiverEmail)
            .filter(friendship -> friendship.getFriendshipStatus() == FriendshipStatusEnum.PENDING)
            .map(friendship -> {
                friendshipStatusRepository.delete(friendship);
                return true;
            })
            .orElse(false);
    }

    private FriendshipStatusDTO convertToDTO(FriendshipStatus friendship) {
        return new FriendshipStatusDTO(
            friendship.getId(),
            friendship.getSender().getEmail(),
            friendship.getReceiver().getEmail(),
            friendship.getReceiver().getUsername(),
            friendship.getFriendshipStatus()
        );
    }
}

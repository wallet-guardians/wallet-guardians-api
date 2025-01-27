package com.walletguardians.walletguardiansapi.domain.friend.service;

import com.walletguardians.walletguardiansapi.domain.friend.entity.FriendshipStatus;
import com.walletguardians.walletguardiansapi.domain.friend.repository.FriendshipStatusRepository;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final FriendshipStatusRepository friendshipStatusRepository;

    // 친구 요청 보내기
    public FriendshipStatus sendFriendRequest(User sender, User receiver) {
        FriendshipStatus friendshipStatus = FriendshipStatus.builder()
            .sender(sender)
            .receiver(receiver)
            .friendshipStatus(false) // 기본적으로 요청 상태
            .build();

        return friendshipStatusRepository.save(friendshipStatus);
    }

    // 친구 요청 수락
    public FriendshipStatus acceptFriendRequest(FriendshipStatus friendshipStatus) {
        friendshipStatus.setFriendshipStatus(true); // 상태를 친구로 변경
        return friendshipStatusRepository.save(friendshipStatus);
    }

    // 내가 팔로잉하는 사용자 목록 조회
    public List<FriendshipStatus> getFollowingList(User user) {
        return friendshipStatusRepository.findBySender(user);
    }

    // 나를 팔로우하는 사용자 목록 조회
    public List<FriendshipStatus> getFollowerList(User user) {
        return friendshipStatusRepository.findByReceiver(user);
    }

    public FriendshipStatus findById(Long id) {
        return friendshipStatusRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 친구 요청이 없습니다."));
    }
}

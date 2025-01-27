package com.walletguardians.walletguardiansapi.domain.friend.repository;

import com.walletguardians.walletguardiansapi.domain.friend.entity.FriendshipStatus;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendshipStatusRepository extends JpaRepository<FriendshipStatus, Long> {
    // 내가 팔로잉하는 목록 조회
    List<FriendshipStatus> findBySender(User sender);

    // 나를 팔로우하는 목록 조회
    List<FriendshipStatus> findByReceiver(User receiver);
}

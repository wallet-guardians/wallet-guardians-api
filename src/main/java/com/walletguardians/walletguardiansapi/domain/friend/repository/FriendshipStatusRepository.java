package com.walletguardians.walletguardiansapi.domain.friend.repository;

import com.walletguardians.walletguardiansapi.domain.friend.entity.FriendshipStatus;
import com.walletguardians.walletguardiansapi.domain.friend.entity.FriendshipStatusEnum;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface FriendshipStatusRepository extends JpaRepository<FriendshipStatus, Long> {

    //특정 사용자가 보낸 친구 요청 조회 (모든 상태 포함)
    List<FriendshipStatus> findBySender(User sender);

    //특정 사용자가 받은 친구 요청 조회 (모든 상태 포함)
    List<FriendshipStatus> findByReceiver(User receiver);

    //특정 두 사용자 간의 친구 요청을 찾음
    Optional<FriendshipStatus> findBySenderAndReceiver(User sender, User receiver);

    //특정 두 사용자 간의 친구 요청을 이메일 기준으로 찾음
    @Query("SELECT f FROM FriendshipStatus f " +
        "WHERE f.sender.email = :senderEmail AND f.receiver.email = :receiverEmail")
    Optional<FriendshipStatus> findBySenderAndReceiverEmail(
        @Param("senderEmail") String senderEmail,
        @Param("receiverEmail") String receiverEmail
    );

    //친구 요청을 수락하여 ACCEPTED 상태로 변경
    @Modifying
    @Query("UPDATE FriendshipStatus f " +
        "SET f.friendshipStatus = :status " +
        "WHERE f.sender = :sender AND f.receiver = :receiver")
    void acceptFriendRequest(
        @Param("sender") User sender,
        @Param("receiver") User receiver,
        @Param("status") FriendshipStatusEnum status
    );

    //친구 요청(PENDING 상태)을 거절하여 요청을 삭제
    @Modifying
    @Query("DELETE FROM FriendshipStatus f " +
        "WHERE f.sender = :sender AND f.receiver = :receiver " +
        "AND f.friendshipStatus = :status")
    void rejectFriendRequest(
        @Param("sender") User sender,
        @Param("receiver") User receiver,
        @Param("status") FriendshipStatusEnum status
    );

    //친구 관계(ACCEPTED 상태) 삭제 (양방향 지원)
    @Modifying
    @Query("DELETE FROM FriendshipStatus f " +
        "WHERE (f.sender = :user1 AND f.receiver = :user2) " +
        "OR (f.sender = :user2 AND f.receiver = :user1) " +
        "AND f.friendshipStatus = :status")
    void deleteFriendship(
        @Param("user1") User user1,
        @Param("user2") User user2,
        @Param("status") FriendshipStatusEnum status
    );

    //내가 보낸 친구 요청 중 PENDING 상태 조회
    @Query("SELECT f FROM FriendshipStatus f " +
        "WHERE f.sender.email = :userEmail " +
        "AND f.friendshipStatus = :status")
    List<FriendshipStatus> findPendingRequestsBySender(
        @Param("userEmail") String userEmail,
        @Param("status") FriendshipStatusEnum status
    );

    //나와 친구 상태인 사용자 목록 조회 (ACCEPTED 상태)
    @Query("SELECT f FROM FriendshipStatus f " +
        "WHERE (f.sender.email = :userEmail OR f.receiver.email = :userEmail) " +
        "AND f.friendshipStatus = :status")
    List<FriendshipStatus> findAcceptedFriends(
        @Param("userEmail") String userEmail,
        @Param("status") FriendshipStatusEnum status
    );
}

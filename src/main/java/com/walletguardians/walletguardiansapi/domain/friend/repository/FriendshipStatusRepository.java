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

import java.util.List;
import java.util.Optional;

public interface FriendshipStatusRepository extends JpaRepository<FriendshipStatus, Long> {

    List<FriendshipStatus> findBySender(User sender);

    List<FriendshipStatus> findByReceiver(User receiver);

    Optional<FriendshipStatus> findBySenderAndReceiver(User sender, User receiver);

    @Query("SELECT f FROM FriendshipStatus f " +
        "WHERE f.sender.email = :senderEmail AND f.receiver.email = :receiverEmail")
    Optional<FriendshipStatus> findBySenderAndReceiverEmail(
        @Param("senderEmail") String senderEmail,
        @Param("receiverEmail") String receiverEmail
    );

    @Query("SELECT f FROM FriendshipStatus f " +
        "WHERE f.sender.email = :userEmail " +
        "AND f.friendshipStatus = :status")
    List<FriendshipStatus> findPendingRequestsBySender(
        @Param("userEmail") String userEmail,
        @Param("status") FriendshipStatusEnum status
    );

    @Query("SELECT f FROM FriendshipStatus f " +
        "WHERE f.receiver.email = :userEmail " +
        "AND f.friendshipStatus = :status")
    List<FriendshipStatus> findPendingRequestsByReceiver(
        @Param("userEmail") String userEmail,
        @Param("status") FriendshipStatusEnum status
    );

    @Query("SELECT f FROM FriendshipStatus f " +
        "WHERE (f.sender.email = :userEmail OR f.receiver.email = :userEmail) " +
        "AND f.friendshipStatus = :status")
    List<FriendshipStatus> findAcceptedFriends(
        @Param("userEmail") String userEmail,
        @Param("status") FriendshipStatusEnum status
    );
}

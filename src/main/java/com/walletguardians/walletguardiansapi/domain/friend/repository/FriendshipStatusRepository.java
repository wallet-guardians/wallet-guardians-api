package com.walletguardians.walletguardiansapi.domain.friend.repository;

import com.walletguardians.walletguardiansapi.domain.friend.entity.FriendshipStatus;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FriendshipStatusRepository extends JpaRepository<FriendshipStatus, Long> {

    List<FriendshipStatus> findBySender(User sender);
    List<FriendshipStatus> findByReceiver(User receiver);
    Optional<FriendshipStatus> findBySenderAndReceiver(User sender, User receiver);

    @Query("SELECT DISTINCT f FROM FriendshipStatus f WHERE f.sender = :user")
    List<FriendshipStatus> findBySenderDistinct(@Param("user") User user);

    @Query("SELECT DISTINCT f FROM FriendshipStatus f WHERE f.receiver = :user")
    List<FriendshipStatus> findByReceiverDistinct(@Param("user") User user);

    @Query("SELECT f FROM FriendshipStatus f WHERE f.sender.email = :senderEmail AND f.receiver.email = :receiverEmail")
    Optional<FriendshipStatus> findBySenderAndReceiverEmail(@Param("senderEmail") String senderEmail, @Param("receiverEmail") String receiverEmail);

    @Modifying
    @Query("UPDATE FriendshipStatus f SET f.friendshipStatus = true WHERE f.sender = :sender AND f.receiver = :receiver")
    void acceptFriendRequest(@Param("sender") User sender, @Param("receiver") User receiver);
}

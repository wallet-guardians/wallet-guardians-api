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

    List<FriendshipStatus> findBySender(User sender);
    List<FriendshipStatus> findByReceiver(User receiver);
    Optional<FriendshipStatus> findBySenderAndReceiver(User sender, User receiver);

    @Query("SELECT f FROM FriendshipStatus f WHERE f.sender.email = :senderEmail AND f.receiver.email = :receiverEmail")
    Optional<FriendshipStatus> findBySenderAndReceiverEmail(@Param("senderEmail") String senderEmail, @Param("receiverEmail") String receiverEmail);

    @Modifying
    @Query("UPDATE FriendshipStatus f SET f.friendshipStatus = 'ACCEPTED' WHERE f.sender = :sender AND f.receiver = :receiver")
    void acceptFriendRequest(@Param("sender") User sender, @Param("receiver") User receiver);

    @Modifying
    @Query("DELETE FROM FriendshipStatus f WHERE f.sender = :sender AND f.receiver = :receiver")
    void rejectFriendRequest(@Param("sender") User sender, @Param("receiver") User receiver);

    @Modifying
    @Query("DELETE FROM FriendshipStatus f WHERE (f.sender = :user1 AND f.receiver = :user2) OR (f.sender = :user2 AND f.receiver = :user1)")
    void deleteFriendship(@Param("user1") User user1, @Param("user2") User user2);}

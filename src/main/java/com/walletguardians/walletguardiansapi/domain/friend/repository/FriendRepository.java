package com.walletguardians.walletguardiansapi.domain.friend.repository;

import com.walletguardians.walletguardiansapi.domain.friend.entity.Friend;
import com.walletguardians.walletguardiansapi.domain.friend.entity.status.FriendStatusEnum;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

  Optional<Friend> findBySenderAndReceiver(User sender, User receiver);

  boolean existsBySenderAndReceiver(User sender, User receiver);

  @Query("SELECT f FROM Friend f " +
      "WHERE f.sender.email = :userEmail " +
      "AND f.friendStatus = :status")
  List<Friend> findPendingRequestsBySender(
      @Param("userEmail") String userEmail,
      @Param("status") FriendStatusEnum status
  );

  @Query("SELECT f FROM Friend f " +
      "WHERE f.receiver.email = :userEmail " +
      "AND f.friendStatus = :status")
  List<Friend> findPendingRequestsByReceiver(
      @Param("userEmail") String userEmail,
      @Param("status") FriendStatusEnum status
  );

  @Query("SELECT f FROM Friend f " +
      "WHERE (f.sender.email = :userEmail OR f.receiver.email = :userEmail) " +
      "AND f.friendStatus = :status")
  List<Friend> findAcceptedFriends(
      @Param("userEmail") String userEmail,
      @Param("status") FriendStatusEnum status
  );
}

package com.walletguardians.walletguardiansapi.domain.friend.repository;

import com.walletguardians.walletguardiansapi.domain.friend.entity.FriendStatus;
import com.walletguardians.walletguardiansapi.domain.friend.entity.status.FriendStatusEnum;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FriendStatusRepository extends JpaRepository<FriendStatus, Long> {

  List<FriendStatus> findBySender(User sender);

  List<FriendStatus> findByReceiverAndFriendStatus(User receiver, FriendStatusEnum friendStatus);

  Optional<FriendStatus> findByIdAndReceiver(Long id, User sender);

  boolean existsBySenderAndReceiver(User sender, User receiver);

  Optional<FriendStatus> findByIdAndSender(Long id, User sender);
}

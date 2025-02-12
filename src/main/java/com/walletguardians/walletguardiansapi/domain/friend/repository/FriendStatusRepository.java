package com.walletguardians.walletguardiansapi.domain.friend.repository;

import com.walletguardians.walletguardiansapi.domain.friend.entity.FriendState;
import com.walletguardians.walletguardiansapi.domain.friend.entity.status.FriendStatusEnum;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FriendStatusRepository extends JpaRepository<FriendState, Long> {

  List<FriendState> findBySender(User sender);

  List<FriendState> findByReceiverAndFriendStatus(User receiver, FriendStatusEnum friendStatus);

  Optional<FriendState> findByIdAndReceiver(Long id, User sender);

  boolean existsBySenderAndReceiver(User sender, User receiver);

  Optional<FriendState> findByIdAndSender(Long id, User sender);
}

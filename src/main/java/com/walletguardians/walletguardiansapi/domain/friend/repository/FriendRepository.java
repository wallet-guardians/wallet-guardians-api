package com.walletguardians.walletguardiansapi.domain.friend.repository;

import com.walletguardians.walletguardiansapi.domain.friend.entity.Friend;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {

  List<Friend> findByUserEntity(User user);

  boolean existsByUserEntityAndFriendEntity(User userEntity, User friendEntity);

  Optional<Friend> findByIdAndUserEntity(Long id, User userEntity);

  void deleteByUserEntityAndFriendEntity(User user, User friend);
}

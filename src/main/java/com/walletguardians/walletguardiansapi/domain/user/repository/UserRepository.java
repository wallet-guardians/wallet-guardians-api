package com.walletguardians.walletguardiansapi.domain.user.repository;

import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

}

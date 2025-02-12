package com.walletguardians.walletguardiansapi.global.auth.jwt.repository;

import com.walletguardians.walletguardiansapi.global.auth.jwt.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
  Optional<RefreshToken> findByUserEmail(String userEmail);

}

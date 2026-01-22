package org.praveenit.bookfloww.repository;

import org.praveenit.bookfloww.entity.RefreshTokenEntity;
import org.praveenit.bookfloww.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    public Optional<RefreshTokenEntity> findByTokenId(String tokenId);
    
    public Optional<RefreshTokenEntity> findTopByUserAndStatusOrderByIdDesc(
            User user,
            RefreshTokenEntity.TokenStatus status
    );
}

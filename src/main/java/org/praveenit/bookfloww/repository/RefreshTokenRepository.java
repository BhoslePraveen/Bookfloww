package org.praveenit.bookfloww.repository;

import java.util.List;
import org.praveenit.bookfloww.entity.RefreshTokenEntity;
import org.praveenit.bookfloww.entity.RefreshTokenEntity.TokenStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long>{
	 //for hashed refresh token verification
    List<RefreshTokenEntity> findAllByStatus(TokenStatus status);

}

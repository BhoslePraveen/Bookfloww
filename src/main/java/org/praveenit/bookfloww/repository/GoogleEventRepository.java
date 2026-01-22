package org.praveenit.bookfloww.repository;

import java.util.List;
import java.util.Optional;
import org.praveenit.bookfloww.entity.GoogleEventEntity;
import org.praveenit.bookfloww.entity.User;
import org.praveenit.bookfloww.enums.EventSource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoogleEventRepository extends JpaRepository<GoogleEventEntity, Long>{
	public Optional<GoogleEventEntity> findByGoogleEventIdAndUser_Id(
            String googleEventId, String userId
    );
	
	public List<GoogleEventEntity> findByUser_Id(String userId);
	
	public long countByUser_Id(String userId);

    public long countByUser_IdAndSource(String userId, EventSource source);

}

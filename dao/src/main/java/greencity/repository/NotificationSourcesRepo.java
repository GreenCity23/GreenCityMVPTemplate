package greencity.repository;

import greencity.entity.NotificationSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationSourcesRepo extends JpaRepository<NotificationSource, Long> {
}

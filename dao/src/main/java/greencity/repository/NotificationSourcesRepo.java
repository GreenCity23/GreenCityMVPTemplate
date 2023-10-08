package greencity.repository;

import greencity.entity.NotificationSource;
import greencity.enums.NotificationSourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationSourcesRepo extends JpaRepository<NotificationSource, Long> {

    NotificationSource findBySource(NotificationSourceType source);
}

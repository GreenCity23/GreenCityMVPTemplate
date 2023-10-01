package greencity.service;

import greencity.dto.notificationsource.NotificationSourceDto;

import java.util.List;
import java.util.Optional;

public interface NotificationSourceService {
    List<NotificationSourceDto> getAllSources();

    Optional<NotificationSourceDto> getSourceById(Long id);

    NotificationSourceDto saveSource(NotificationSourceDto source);

    void deleteSource(Long id);


}

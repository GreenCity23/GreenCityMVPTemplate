package greencity.service;

import greencity.dto.notification.NotificationDto;
import greencity.dto.notification.NotificationDtoResponse;

import java.util.List;
import java.util.Optional;

public interface NotificationService {
    List<NotificationDto> findAll();

    Optional<NotificationDto> findById(Long id);

    public List<NotificationDto> createEcoNewsCommentNotification(Long id);

    List<NotificationDtoResponse> getNotificationsForUser(Long id);

    NotificationDto save(NotificationDto notification);

    void delete(Long id);

    List<NotificationDto> findAllBySenderId(Long id);

    List<NotificationDto> findThreeLastNotificationsByUserId(Long id);

    List<NotificationDto> findAllByNotifiedUserId(Long id);

    List<NotificationDto> findAllByUserIdAndSourceId(Long userId, Long sourceId);

    List<NotificationDto> findAllBySourceId(Long id);
}


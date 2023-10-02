package greencity.service;

import greencity.dto.notification.NotificationDto;

import java.util.List;
import java.util.Optional;

public interface NotificationService {
    List<NotificationDto> getAllNotifications();

    Optional<NotificationDto> getNotificationById(Long id);

    NotificationDto saveNotification(NotificationDto notification);

    void deleteNotification(Long id);

    List<NotificationDto> getAllBySenderId(Long userId);

    List<NotificationDto> getThreeLastNotificationsByUserId(Long userId);

    List<NotificationDto> getAllByNotifiedUserId(Long userId);

    List<NotificationDto> getAllByUserIdAndSourceId(Long userId, Long sourceId);

    List<NotificationDto> getAllBySourceId(Long sourceId);
}


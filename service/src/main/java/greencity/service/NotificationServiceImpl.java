package greencity.service;

import greencity.dto.notification.NotificationDto;
import greencity.repository.NotificationRepo;
import greencity.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepo notificationRepo;

    @Autowired
    public NotificationServiceImpl(NotificationRepo notificationRepo) {
        this.notificationRepo = notificationRepo;

    }

    @Override
    public List<NotificationDto> getAllNotifications() {
        List<Notification> notifications = notificationRepo.findAll();
        return null;
    }

    @Override
    public Optional<NotificationDto> getNotificationById(Long id) {
        return null;

    }

    @Override
    public NotificationDto saveNotification(NotificationDto notificationDto) {
        return null;
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepo.deleteById(id);
    }



}

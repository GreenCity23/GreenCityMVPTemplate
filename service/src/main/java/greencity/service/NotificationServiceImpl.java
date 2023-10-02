package greencity.service;

import greencity.dto.notification.NotificationDto;
import greencity.mapping.NotificationDtoMapper;
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
    private final NotificationDtoMapper notificationDtoMapper;

    @Autowired
    public NotificationServiceImpl(NotificationRepo notificationRepo, NotificationDtoMapper notificationDtoMapper) {
        this.notificationRepo = notificationRepo;
        this.notificationDtoMapper = notificationDtoMapper;
    }

    @Override
    public List<NotificationDto> getAllNotifications() {
        return mapList(notificationRepo.findAll());
    }

    @Override
    public Optional<NotificationDto> getNotificationById(Long id) {
        return notificationRepo.findById(id).map(notificationDtoMapper::convertToDto);

    }

    @Override
    public List<NotificationDto> getAllBySenderId(Long userId) {
        return mapList(notificationRepo.findAllBySenderId(userId));
    }

    @Override
    public NotificationDto saveNotification(NotificationDto notificationDto) {
        Notification savedNotification = notificationRepo.save(notificationDtoMapper.convertToEntity(notificationDto));
        return notificationDtoMapper.convertToDto(savedNotification);
    }

    @Override
    public List<NotificationDto> getThreeLastNotificationsByUserId(Long userId) {
        return mapList(notificationRepo.findThreeLastNotificationsByUserId(userId));
    }

    @Override
    public List<NotificationDto> getAllByNotifiedUserId(Long userId) {
        return mapList(notificationRepo.findAllByNotifiedUserId(userId));
    }

    @Override
    public List<NotificationDto> getAllByUserIdAndSourceId(Long userId, Long sourceId) {
        return mapList(notificationRepo.findAllByUserIdAndSourceId(userId, sourceId));
    }

    @Override
    public List<NotificationDto> getAllBySourceId(Long sourceId) {
        return mapList(notificationRepo.findAllBySourceId(sourceId));
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepo.deleteById(id);
    }

    private List<NotificationDto> mapList(List<Notification> notifications) {
        return notifications.stream()
            .map(notificationDtoMapper::convertToDto)
            .collect(Collectors.toList());
    }
}

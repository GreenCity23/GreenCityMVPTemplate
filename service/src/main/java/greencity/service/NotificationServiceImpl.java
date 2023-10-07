package greencity.service;

import greencity.dto.notification.NotificationDto;
import greencity.mapping.NotificationDtoMapper;
import greencity.repository.NotificationRepo;
import greencity.entity.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for {@link Notification} entity.
 */
@AllArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepo notificationRepo;
    private final NotificationDtoMapper notificationDtoMapper;

    /**
     * Method for getting all notifications.
     *
     * @return List of {@link NotificationDto} instances.
     * @author Nazar Klimovych
     */
    @Override
    public List<NotificationDto> findAll() {
        return mapList(notificationRepo.findAll());
    }

    /**
     * Method for getting notification by id.
     *
     * @param id {@link Long} notification id.
     * @return List of {@link NotificationDto} instances.
     * @author Nazar Klimovych
     */
    @Override
    public Optional<NotificationDto> findById(Long id) {
        return notificationRepo.findById(id).map(notificationDtoMapper::convertToDto);
    }

    /**
     * Method for getting all notifications by sender id.
     *
     * @param id {@link Long} sender id.
     * @return List of {@link NotificationDto} instances.
     * @author Nazar Klimovych
     */
    @Override
    public List<NotificationDto> findAllBySenderId(Long id) {
        return mapList(notificationRepo.findAllBySenderId(id));
    }

    /**
     * Method for saving notification to a database.
     *
     * @param notificationDto {@link NotificationDto} .
     * @return {@link Notification} instance.
     * @author Nazar Klimovych
     */
    @Override
    public NotificationDto save(NotificationDto notificationDto) {
        Notification notification = notificationDtoMapper.convertToEntity(notificationDto);
        Notification savedNotification = notificationRepo.save(notification);
        return notificationDtoMapper.convertToDto(savedNotification);
    }

    /**
     * Method for getting the last three notifications of a specific user.
     *
     * @param id {@link Long} user id.
     * @return List of {@link NotificationDto} instances.
     * @author Nazar Klimovych
     */
    @Override
    public List<NotificationDto> findThreeLastNotificationsByUserId(Long id) {
        return mapList(notificationRepo.findThreeLastNotificationsByUserId(id));
    }

    /**
     * Method for getting all notifications for a notified user.
     *
     * @param id {@link Long} notified user id.
     * @return List of {@link NotificationDto} instances.
     * @author Nazar Klimovych
     */
    @Override
    public List<NotificationDto> findAllByNotifiedUserId(Long id) {
        return mapList(notificationRepo.findAllByNotifiedUserId(id));
    }

    /**
     * Method for getting all notifications for a notified user
     * by notification source.
     *
     * @param userId {@link Long} notified user id.
     * @param sourceId {@link Long} notification source id.
     * @return list of {@link Notification} instances.
     * @author Nazar Klimovych
     */
    @Override
    public List<NotificationDto> findAllByUserIdAndSourceId(Long userId, Long sourceId) {
        return mapList(notificationRepo.findAllByUserIdAndSourceId(userId, sourceId));
    }

    /**
     * Method for getting all notifications by notification source.
     *
     * @param id {@link Long} notification source id.
     * @return list of {@link Notification} instances.
     * @author Nazar Klimovych
     */
    @Override
    public List<NotificationDto> findAllBySourceId(Long id) {
        return mapList(notificationRepo.findAllBySourceId(id));
    }

    /**
     * Method for deleting notification from a database.
     *
     * @param id {@link Long} notification id.
     * @return {@link Long} id of deleted notification.
     * @author Nazar Klimovych
     */
    @Override
    public Long delete(Long id) {
        notificationRepo.deleteById(id);
        return id;
    }

    /**
     * Maps a list of {@link Notification} objects to a list of {@link NotificationDto} objects.
     *
     * @param notifications the list of {@link Notification} objects to be mapped.
     * @return a list of {@link NotificationDto} objects.
     */
    private List<NotificationDto> mapList(List<Notification> notifications) {
        return notifications.stream()
            .map(notificationDtoMapper::convertToDto)
            .collect(Collectors.toList());
    }
}

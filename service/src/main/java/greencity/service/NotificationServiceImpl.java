package greencity.service;

import greencity.constant.ErrorMessage;
import greencity.dto.notification.NotificationDto;
import greencity.dto.notification.NotificationDtoResponse;
import greencity.enums.NotificationSourceType;
import greencity.exception.exceptions.NotFoundException;
import greencity.mapping.NotificationDtoMapper;
import greencity.mapping.NotificationDtoResponseMapper;
import greencity.repository.EcoNewsCommentRepo;
import greencity.repository.NotificationRepo;
import greencity.entity.*;
import greencity.repository.NotificationSourcesRepo;
import greencity.repository.NotifiedUserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for {@link Notification} entity.
 */
@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepo notificationRepo;
    private final EcoNewsCommentRepo ecoNewsCommentRepo;
    private final NotificationSourcesRepo notificationSourcesRepo;
    private final NotificationDtoMapper notificationDtoMapper;
    private final NotificationDtoResponseMapper notificationDtoResponseMapper;
    private final NotifiedUserRepo notifiedUserRepo;

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

    @Override
    public List<NotificationDto> createEcoNewsCommentNotification(Long Id){
        EcoNewsComment comment = ecoNewsCommentRepo.findById(Id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.COMMENT_NOT_FOUND_EXCEPTION));

        List<Notification> notifications = new ArrayList<>();

        Notification ecoNewsCommentNotification = createNotification(
                comment.getUser(),
                comment.getEcoNews().getTitle(),
                NotificationSourceType.NEWS_COMMENTED,
                comment.getEcoNews().getAuthor()
        );
        notifications.add(notificationRepo.save(ecoNewsCommentNotification));

        if (comment.getParentComment() != null) {
            Notification replyCommentNotification = createNotification(
                    comment.getUser(),
                    comment.getEcoNews().getTitle(),
                    NotificationSourceType.COMMENT_REPLY,
                    comment.getParentComment().getUser()
            );
            notifications.add(notificationRepo.save(replyCommentNotification));
        }

        comment.setNotifications(notifications);
        ecoNewsCommentRepo.save(comment);

        return mapList(notifications);
    }

    private Notification createNotification(User sender, String title, NotificationSourceType sourceType, User recipient) {
        Notification notification = new Notification();
        notification.setCreationDate(ZonedDateTime.now());
        notification.setTitle(title);
        notification.setSender(sender);
        notification.setSource(notificationSourcesRepo.findBySource(sourceType));

        NotifiedUser notifiedUser = new NotifiedUser();
        notifiedUser.setUser(recipient);
        notifiedUser.setIsRead(false);
        notifiedUser.setNotification(notification);

        List<NotifiedUser> notifiedUsers = new ArrayList<>();
        notifiedUsers.add(notifiedUser);

        notification.setNotifiedUsers(notifiedUsers);

        return notification;
    }

    @Override
    public List<NotificationDtoResponse> getNotificationsForUser(Long userId) {
        List<NotifiedUser> notifiedUsers = notifiedUserRepo.findAllByUserId(userId);
        return notifiedUsers.stream()
                .map(notificationDtoResponseMapper::convert)
                .collect(Collectors.toList());
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

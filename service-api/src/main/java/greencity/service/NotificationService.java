package greencity.service;

import greencity.dto.PageableDto;
import greencity.dto.notification.NotificationDto;
import greencity.dto.notification.NotificationDtoResponse;
import greencity.dto.user.UserVO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Provides the interface to manage {@code Notification} entity.
 */
public interface NotificationService {

    List<NotificationDto> createEcoNewsCommentNotification(Long id);

    List<NotificationDtoResponse> getNotificationsForUser(Long id);

    /**
     * Method for getting a list of all notifications.
     *
     * @return List of {@link NotificationDto} instances.
     * @author Nazar Klimovych
     */
    PageableDto<NotificationDto> findAll(Pageable pageable);

    /**
     * Method for getting notification by id.
     *
     * @param id {@link Long} notification id.
     * @return List of {@link NotificationDto} instances.
     * @author Nazar Klimovych
     */
    NotificationDto findById(Long id);

    /**
     * Method for saving notification to a database.
     *
     * @param notification {@link NotificationDto} .
     * @return {@code Notification} instance.
     * @author Nazar Klimovych
     */
    NotificationDto save(NotificationDto notification);

    /**
     * Method for deleting notification from a database.
     *
     * @param id {@link Long} notification id.
     * @return {@link Long} id of deleted notification.
     * @author Nazar Klimovych
     */
    Long delete(Long id, UserVO user);

    /**
     * Method for getting all notifications by sender id.
     *
     * @param id {@link Long} sender id.
     * @return List of {@link NotificationDto} instances.
     * @author Nazar Klimovych
     */
    PageableDto<NotificationDto> findAllBySenderId(Pageable pageable, Long id);

    /**
     * Method for getting the last three notifications of a specific user.
     *
     * @param id {@link Long} user id.
     * @return List of {@link NotificationDto} instances.
     * @author Nazar Klimovych
     */
    List<NotificationDto> findThreeLastNotificationsByUserId(Long id);

    /**
     * Method for getting all notifications for a notified user.
     *
     * @param id {@link Long} notified user id.
     * @return List of {@link NotificationDto} instances.
     * @author Nazar Klimovych
     */
    PageableDto<NotificationDto> findAllByNotifiedUserId(Pageable pageable, Long id);

    /**
     * Method for getting all notifications for a notified user
     * by notification source.
     *
     * @param userId {@link Long} notified user id.
     * @param sourceId {@link Long} notification source id.
     * @return list of {@code Notification} instances.
     * @author Nazar Klimovych
     */
    PageableDto<NotificationDto> findAllByUserIdAndSourceId(Pageable pageable, Long userId, Long sourceId);

    /**
     * Method for getting all notifications by notification source.
     *
     * @param id {@link Long} notification source id.
     * @return list of {@code Notification} instances.
     * @author Nazar Klimovych
     */
    PageableDto<NotificationDto> findAllBySourceId(Pageable pageable, Long id);
}



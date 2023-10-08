package greencity.repository;

import greencity.dto.notification.NotificationDtoResponse;
import greencity.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Provides an interface to manage {@link Notification} entity.
 */
@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {

    /**
     * Method for getting all notifications by sender id.
     *
     * @param userId {@link Long} sender id.
     * @return list of {@link Notification} instances.
     * @author Nazar Klimovych
     */
    List<Notification> findAllBySenderId(Long userId);

    /**
     * Method for getting the last three notifications of a specific user.
     *
     * @param userId {@link Long} notified user id.
     * @return list of {@link Notification} instances.
     * @author Nazar Klimovych
     */
    @Query(nativeQuery = true, value = "SELECT n.* "
        + "FROM notifications n "
        + "INNER JOIN notified_users nu ON n.id = nu.notification_id "
        + "WHERE nu.user_id = :userId "
        + "ORDER BY n.creation_date DESC "
        + "LIMIT 3")
    List<Notification> findThreeLastNotificationsByUserId(@Param("userId") Long userId);

    /**
     * Method for getting all notifications for a notified user.
     *
     * @param userId {@link Long} notified user id.
     * @return list of {@link Notification} instances.
     * @author Nazar Klimovych
     */
    @Query(nativeQuery = true, value = "SELECT n.* "
        + "FROM notifications n "
        + "INNER JOIN notified_users nu ON n.id = nu.notification_id "
        + "WHERE nu.user_id = :userId "
        + "ORDER BY n.creation_date")
    List<Notification> findAllByNotifiedUserId(@Param("userId") Long userId);

    /**
     * Method for getting all notifications for a notified user
     * by notification source.
     *
     * @param userId {@link Long} notified user id.
     * @param sourceId {@link Long} notification source id.
     * @return list of {@link Notification} instances.
     * @author Nazar Klimovych
     */
    @Query(nativeQuery = true, value = "SELECT n.* "
        + "FROM notifications n "
        + "INNER JOIN notified_users nu ON n.id = nu.notification_id "
        + "WHERE nu.user_id = :userId "
        + "AND n.source_id = :sourceId "
        + "ORDER BY n.creation_date")
    List<Notification> findAllByUserIdAndSourceId(@Param("userId") Long userId, @Param("sourceId") Long sourceId);

    @Query(nativeQuery = true, value =
            "SELECT " +
//                    "    u.name AS userName, " +
//                    "    ns.source AS action, " +
                    "    n.title AS title " +
//                    "    TO_CHAR(n.creation_date, 'DD.MM.YYYY HH:MI XM') AS creationDate, " +
//                    "    nu.is_read AS isRead " +
                    "FROM notifications n " +
                    "JOIN notification_sources ns ON n.source_id = ns.id " +
                    "JOIN notified_users nu ON n.id = nu.notification_id " +
                    "JOIN users u ON n.sender_id = u.id " +
                    "WHERE nu.user_id = :userId " +
                    "ORDER BY n.creation_date DESC")
    List<NotificationDtoResponse> getNotificationsForUser(@Param("userId") Long userId);

    /**
     * Method for getting all notifications by notification source.
     *
     * @param id {@link Long} notification source id.
     * @return list of {@link Notification} instances.
     * @author Nazar Klimovych
     */
    List<Notification> findAllBySourceId(@Param("id") Long id);
}

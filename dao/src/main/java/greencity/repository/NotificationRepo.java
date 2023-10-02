package greencity.repository;

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

    /**
     * Method for getting all notifications by notification source.
     *
     * @param sourceId {@link Long} notification source id.
     * @return list of {@link Notification} instances.
     * @author Nazar Klimovych
     */
    List<Notification> findAllBySourceId(@Param("sourceId") Long sourceId);
}

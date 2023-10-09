package greencity.repository;

import greencity.entity.NotifiedUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NotifiedUserRepo extends JpaRepository<NotifiedUser, Long>, JpaSpecificationExecutor<NotifiedUser> {

    /**
     * Retrieves a page of NotifiedUser entities based on the provided pageable information and user ID.
     *
     * @param pageable The pageable information for pagination.
     * @param userId   The user ID to filter by.
     * @return A page of NotifiedUser entities.
     */
    Page<NotifiedUser> findAllByUserId(Pageable pageable, Long userId);

    /**
     * Counts the number of unread notifications for a specific user.
     *
     * @param userId The user ID to count unread notifications for.
     * @return The count of unread notifications.
     */
    Long countByUserIdAndIsReadIsFalse(Long userId);

    /**
     * Counts the number of notifications for a specific user and notification ID.
     *
     * @param userId         The user ID to filter by.
     * @param notificationId The notification ID to filter by.
     * @return The count of notifications matching the user and notification ID.
     */
    @Query("SELECT COUNT(nu) FROM NotifiedUser nu WHERE nu.user.id = :userId AND nu.notification.id = :notificationId")
    Long countByUserIdAndNotificationId(Long userId, Long notificationId);

    /**
     * Marks a notification as read for a specific user.
     *
     * @param notificationId The ID of the notification to mark as read.
     * @param userId         The user ID to mark the notification as read for.
     */
    @Transactional
    @Modifying
    @Query("UPDATE NotifiedUser nu SET nu.isRead = true WHERE nu.notification.id = :notificationId AND nu.user.id = :userId")
    void setNotificationAsRead(Long notificationId, Long userId);

    /**
     * Marks a notification as unread for a specific user.
     *
     * @param notificationId The ID of the notification to mark as unread.
     * @param userId         The user ID to mark the notification as unread for.
     */
    @Transactional
    @Modifying
    @Query("UPDATE NotifiedUser nu SET nu.isRead = false WHERE nu.notification.id = :notificationId AND nu.user.id = :userId")
    void setNotificationAsUnread(Long notificationId, Long userId);

    /**
     * Deletes a notification for a specific user.
     *
     * @param userId         The user ID to filter by.
     * @param notificationId The notification ID to filter by.
     */
    @Transactional
    @Modifying
    @Query("DELETE FROM NotifiedUser nu WHERE nu.user.id = :userId AND nu.notification.id = :notificationId")
    void deleteByUserIdAndNotificationId(Long userId, Long notificationId);

}
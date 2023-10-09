package greencity.service;



import greencity.dto.notifieduser.NotifiedUserDto;

import java.util.List;

public interface NotifiedUserService {

    /**
     * Retrieves a list of all NotifiedUserDto entities.
     *
     * @return A list of NotifiedUserDto entities.
     */
    List<NotifiedUserDto> getAllNotifiedUsers();

    /**
     * Retrieves a NotifiedUserDto entity by its ID.
     *
     * @param id The ID of the NotifiedUserDto to retrieve.
     * @return The NotifiedUserDto entity with the specified ID, or null if not found.
     */
    NotifiedUserDto getNotifiedUserById(Long id);

    /**
     * Saves a NotifiedUserDto entity.
     *
     * @param notifiedUserDto The NotifiedUserDto to save.
     * @return The saved NotifiedUserDto entity.
     */
    NotifiedUserDto saveNotifiedUsers(NotifiedUserDto notifiedUserDto);
    /**
     * Counts the number of unread notifications for a specific user.
     *
     * @param userId The user ID to count unread notifications for.
     * @return The count of unread notifications.
     */
    Long countUnreadNotificationsForUser(Long userId);

    /**
     * Marks a notification as read for a specific user.
     *
     * @param notificationId The ID of the notification to mark as read.
     * @param userId         The user ID to mark the notification as read for.
     */
    void setNotificationAsRead(Long notificationId, Long userId);

    /**
     * Marks a notification as unread for a specific user.
     *
     * @param notificationId The ID of the notification to mark as unread.
     * @param userId         The user ID to mark the notification as unread for.
     */
    void setNotificationAsUnread(Long notificationId, Long userId);

    /**
     * Deletes a notification for a specific user.
     *
     * @param userId         The user ID to filter by.
     * @param notificationId The notification ID to filter by.
     */
    void deleteByUserIdAndNotificationId(Long userId, Long notificationId);

}

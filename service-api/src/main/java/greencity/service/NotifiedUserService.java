package greencity.service;



import greencity.dto.notifieduser.NotifiedUserDto;

import java.util.List;

public interface NotifiedUserService {
    List<NotifiedUserDto> getAllNotifiedUsers();

    NotifiedUserDto getNotifiedUserById(Long id);

    NotifiedUserDto saveNotifiedUsers(NotifiedUserDto notifiedUserDto);

    List<NotifiedUserDto> getAllUsersNotifications(Long userId);

    Long countUnreadNotificationsForUser(Long userId);

    void setNotificationAsRead(Long notificationId, Long userId);

    void setNotificationAsUnread(Long notificationId, Long userId);

    void deleteByUserIdAndNotificationId(Long userId, Long notificationId);

    void deleteNotifiedUser(Long id);
}

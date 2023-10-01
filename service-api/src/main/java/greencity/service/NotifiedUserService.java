package greencity.service;



import greencity.dto.notifieduser.NotifiedUserDto;

import java.util.List;
import java.util.Optional;

public interface NotifiedUserService {
    List<NotifiedUserDto> getAllNotifiedUsers();

    Optional<NotifiedUserDto> getNotifiedUserById(Long id);

    NotifiedUserDto saveNotifiedUsers(NotifiedUserDto notifiedUserDto);

    List<NotifiedUserDto> getAllUsersNotifications(Long userId);

    Long countUnreadNotificationsForUser(Long userId);

    void setNotificationAsRead(Long notificationId, Long userId);

    void setNotificationAsUnread(Long notificationId, Long userId);

    void deleteNotifiedUser(Long id);
}

package greencity.repository;

import greencity.entity.NotifiedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotifiedUserRepo extends JpaRepository<NotifiedUser, Long>, JpaSpecificationExecutor<NotifiedUser> {

    List<NotifiedUser> findAllByUserId(Long userId);

    Long countByUserIdAndIsReadIsFalse(Long userId);

    @Query("SELECT COUNT(nu) FROM NotifiedUser nu WHERE nu.user.id = :userId AND nu.notification.id = :notificationId")
    Long countByUserIdAndNotificationId(Long userId, Long notificationId);

    @Transactional
    @Modifying
    @Query("UPDATE NotifiedUser nu SET nu.isRead = true WHERE nu.notification.id = :notificationId AND nu.user.id = :userId")
    void setNotificationAsRead(Long notificationId, Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE NotifiedUser nu SET nu.isRead = false WHERE nu.notification.id = :notificationId AND nu.user.id = :userId")
    void setNotificationAsUnread(Long notificationId, Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM NotifiedUser nu WHERE nu.user.id = :userId AND nu.notification.id = :notificationId")
    void deleteByUserIdAndNotificationId(Long userId, Long notificationId);

}
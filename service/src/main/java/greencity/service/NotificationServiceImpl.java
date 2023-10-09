package greencity.service;

import greencity.constant.ErrorMessage;
import greencity.dto.PageableDto;
import greencity.dto.notification.NotificationDto;
import greencity.dto.notification.NotificationDtoResponse;
import greencity.dto.user.UserVO;
import greencity.entity.EcoNewsComment;
import greencity.entity.Notification;
import greencity.entity.NotifiedUser;
import greencity.entity.User;
import greencity.enums.NotificationSourceType;
import greencity.enums.Role;
import greencity.exception.exceptions.NotFoundException;
import greencity.exception.exceptions.UserHasNoPermissionToAccessException;
import greencity.mapping.NotificationDtoMapper;
import greencity.mapping.NotificationDtoResponseMapper;
import greencity.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private final UserRepo userRepo;

    /**
     * Method for getting all notifications.
     *
     * @return List of {@link NotificationDto} instances.
     * @author Nazar Klimovych
     */
    @Override
    public PageableDto<NotificationDto> findAll(Pageable pageable) {
        return mapToPageableDto(notificationRepo.findAll(pageable));
    }

    /**
     * Method for getting notification by id.
     *
     * @param id {@link Long} notification id.
     * @return List of {@link NotificationDto} instances.
     * @author Nazar Klimovych
     */
    @Override
    public NotificationDto findById(Long id) {
        Notification notification = notificationRepo.findById(id)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOTIFICATION_NOT_FOUND_BY_ID + id));
        return notificationDtoMapper.convertToDto(notification);
    }

    /**
     * Method for getting all notifications by sender id.
     *
     * @param id {@link Long} sender id.
     * @return List of {@link NotificationDto} instances.
     * @author Nazar Klimovych
     */
    @Override
    public PageableDto<NotificationDto> findAllBySenderId(Pageable pageable, Long id) {
        doesUserExist(id);
        return mapToPageableDto(notificationRepo.findAllBySenderId(pageable, id));
    }

    @Override
    public List<NotificationDto> createEcoNewsCommentNotification(Long id){
        EcoNewsComment comment = ecoNewsCommentRepo.findById(id)
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
        return notificationDtoMapper.convertToDto(notificationRepo.save(notification));
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
        doesUserExist(id);
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
    public PageableDto<NotificationDto> findAllByNotifiedUserId(Pageable pageable, Long id) {
        doesUserExist(id);
        return mapToPageableDto(notificationRepo.findAllByNotifiedUserId(pageable, id));
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
    public PageableDto<NotificationDto> findAllByUserIdAndSourceId(Pageable pageable, Long userId, Long sourceId) {
        doesUserExist(userId);
        return mapToPageableDto(notificationRepo.findAllByUserIdAndSourceId(pageable, userId, sourceId));
    }

    /**
     * Method for getting all notifications by notification source.
     *
     * @param id {@link Long} notification source id.
     * @return list of {@link Notification} instances.
     * @author Nazar Klimovych
     */
    @Override
    public PageableDto<NotificationDto> findAllBySourceId(Pageable pageable, Long id) {
        return mapToPageableDto(notificationRepo.findAllBySourceId(pageable, id));
    }

    /**
     * Method for deleting notification from a database.
     *
     * @param id {@link Long} notification id.
     * @param user current {@link UserVO} that wants to delete.
     * @return {@link Long} id of deleted notification.
     * @author Nazar Klimovych
     */
    @Override
    public Long delete(Long id, UserVO user) {
        if (user.getRole() != Role.ROLE_ADMIN) {
            throw new UserHasNoPermissionToAccessException(ErrorMessage.USER_HAS_NO_PERMISSION);
        }
        Notification notification = notificationRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOTIFICATION_NOT_FOUND_BY_ID + id));
        notificationRepo.deleteById(notification.getId());
        return id;
    }

    /**
     * Maps a list of {@link Notification} objects to a list of {@link NotificationDto} objects.
     *
     * @param notifications the list of {@link Notification} objects to be mapped.
     * @return a list of {@link NotificationDto} objects.
     * @author Nazar Klimovych
     */
    private List<NotificationDto> mapList(List<Notification> notifications) {
        return notifications.stream()
            .map(notificationDtoMapper::convertToDto)
            .collect(Collectors.toList());
    }

    /**
     * Maps a Page of {@link Notification} objects to a {@link PageableDto} of {@link NotificationDto} objects.
     *
     * @param pages the page of {@link Notification} objects to be mapped to {@link NotificationDto} objects.
     * @return a {@link PageableDto} containing a list of {@link NotificationDto} objects.
     * @author Nazar Klimovych
     */
    private PageableDto<NotificationDto> mapToPageableDto(Page<Notification> pages) {
        List<NotificationDto> notificationDtos = pages.stream()
            .map(notificationDtoMapper::convertToDto)
            .collect(Collectors.toList());
        return new PageableDto<>(
            notificationDtos,
            pages.getTotalElements(),
            pages.getPageable().getPageNumber(),
            pages.getTotalPages());
    }

    private void doesUserExist(Long id) {
        if (userRepo.findById(id).isEmpty()) {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND_BY_ID + id);
        }
    }
}

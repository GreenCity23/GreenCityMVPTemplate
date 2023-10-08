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


@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepo notificationRepo;
    private final EcoNewsCommentRepo ecoNewsCommentRepo;
    private final NotificationSourcesRepo notificationSourcesRepo;
    private final NotificationDtoMapper notificationDtoMapper;
    private final NotificationDtoResponseMapper notificationDtoResponseMapper;
    private final NotifiedUserRepo notifiedUserRepo;


    @Override
    public List<NotificationDto> getAllNotifications() {
        return mapList(notificationRepo.findAll());
    }

    @Override
    public Optional<NotificationDto> getNotificationById(Long id) {
        return notificationRepo.findById(id).map(notificationDtoMapper::convertToDto);

    }

    @Override
    public List<NotificationDto> getAllBySenderId(Long userId) {
        return mapList(notificationRepo.findAllBySenderId(userId));
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

    @Override
    public NotificationDto saveNotification(NotificationDto notificationDto) {
        Notification savedNotification = notificationRepo.save(notificationDtoMapper.convertToEntity(notificationDto));
        return notificationDtoMapper.convertToDto(savedNotification);
    }

    @Override
    public List<NotificationDto> getThreeLastNotificationsByUserId(Long userId) {
        return mapList(notificationRepo.findThreeLastNotificationsByUserId(userId));
    }

    @Override
    public List<NotificationDto> getAllByNotifiedUserId(Long userId) {
        List<Notification> notifications = notificationRepo.findAllByNotifiedUserId(userId);
        return mapList(notifications);
    }

    @Override
    public List<NotificationDto> getAllByUserIdAndSourceId(Long userId, Long sourceId) {
        return mapList(notificationRepo.findAllByUserIdAndSourceId(userId, sourceId));
    }

    @Override
    public List<NotificationDto> getAllBySourceId(Long sourceId) {
        return mapList(notificationRepo.findAllBySourceId(sourceId));
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepo.deleteById(id);
    }

    private List<NotificationDto> mapList(List<Notification> notifications) {
        return notifications.stream()
            .map(notificationDtoMapper::convertToDto)
            .collect(Collectors.toList());
    }
}

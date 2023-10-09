package greencity.mapping;

import greencity.dto.notification.NotificationDto;
import greencity.entity.Notification;
import greencity.repository.NotificationSourcesRepo;
import greencity.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

/**
 * This class is responsible for mapping between {@link Notification} entities and {@link NotificationDto}.
 */
@AllArgsConstructor
@Component
public class NotificationDtoMapper {
    private NotificationSourcesRepo notificationSourcesRepo;
    private UserRepo userRepo;

    /**
     * Converts a {@link Notification} entity to a {@link NotificationDto}.
     *
     * @param notification The {@link Notification} entity to be converted.
     * @return A {@link NotificationDto} representing the provided entity.
     * @author Nazar Klimovych
     */
    public NotificationDto convertToDto(Notification notification) {
        return NotificationDto.builder()
            .title(notification.getTitle())
            .sourceId(notification.getSource().getId())
            .senderId(notification.getSender().getId())
            .build();
    }

    /**
     * Converts a {@link NotificationDto} to a {@link Notification} entity.
     *
     * @param dto The {@link NotificationDto} to be converted.
     * @return A {@link Notification} entity representing the provided DTO.
     * @author Nazar Klimovych
     */
    public Notification convertToEntity(NotificationDto dto) {
        return Notification.builder()
            .creationDate(ZonedDateTime.now())
            .title(dto.getTitle())
            .source(notificationSourcesRepo.findById(dto.getSourceId()).orElse(null))
            .sender(userRepo.findById(dto.getSenderId()).orElse(null))
            .build();
    }
}

package greencity.mapping;

import greencity.dto.notification.NotificationDto;
import greencity.entity.Notification;
import greencity.repository.NotificationSourcesRepo;
import greencity.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Component
public class NotificationDtoMapper {
    private NotificationSourcesRepo notificationSourcesRepo;
    private UserRepo userRepo;

    public NotificationDto convertToDto(Notification notification) {
        return NotificationDto.builder()
            .title(notification.getTitle())
            .sourceId(notification.getSource().getId())
            .senderId(notification.getSender().getId())
            .build();
    }

    public Notification convertToEntity(NotificationDto dto) {
        return Notification.builder()
            .creationDate(ZonedDateTime.now())
            .title(dto.getTitle())
            .source(notificationSourcesRepo.findById(dto.getSourceId()).orElse(null))
            .sender(userRepo.findById(dto.getSenderId()).orElse(null))
            .build();
    }
}

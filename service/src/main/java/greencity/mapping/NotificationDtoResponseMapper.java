package greencity.mapping;

import greencity.dto.notification.NotificationDtoResponse;
import greencity.entity.NotifiedUser;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class NotificationDtoResponseMapper extends AbstractConverter<NotifiedUser, NotificationDtoResponse> {

    @Override
    public NotificationDtoResponse convert(NotifiedUser notifiedUser) {
        return NotificationDtoResponse.builder()
                .userName(notifiedUser.getNotification().getSender().getName())
                .action(notifiedUser.getNotification().getSource().getEnName())
                .title(notifiedUser.getNotification().getTitle())
                .creationDate(notifiedUser.getNotification().getCreationDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .isRead(notifiedUser.getIsRead())
                .build();
    }
}

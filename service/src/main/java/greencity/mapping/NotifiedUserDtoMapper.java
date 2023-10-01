package greencity.mapping;

import greencity.dto.notifieduser.NotifiedUserDto;
import greencity.entity.NotifiedUser;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class NotifiedUserDtoMapper extends AbstractConverter<NotifiedUser, NotifiedUserDto> {

    @Override
    protected NotifiedUserDto convert(NotifiedUser notifiedUser) {
        return NotifiedUserDto.builder()
                .id(notifiedUser.getId())
                .notificationId(notifiedUser.getNotification().getId())
                .isRead(notifiedUser.getIsRead())
                .userId(notifiedUser.getId())
                .build();
    }
}

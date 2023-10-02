package greencity.mapping;

import greencity.dto.notification.NotificationDto;
import greencity.entity.Notification;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class NotificationDtoMapper {
    private final ModelMapper modelMapper;

    public NotificationDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public NotificationDto convertToDto(Notification notification) {
        return modelMapper.map(notification, NotificationDto.class);
    }

    public Notification convertToEntity(NotificationDto notificationDto) {
        return modelMapper.map(notificationDto, Notification.class);
    }
}

package greencity.dto.notification;
import greencity.dto.notificationsource.NotificationSourceDto;
import greencity.dto.notifieduser.NotifiedUserDto;
import greencity.dto.user.UserVO;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class NotificationDto {
    private Long id;
    private ZonedDateTime creationDate;
    private String title;
    private NotificationSourceDto source;
    private UserVO sender;
    private List<NotifiedUserDto> notifiedUsers = new ArrayList<>();
}

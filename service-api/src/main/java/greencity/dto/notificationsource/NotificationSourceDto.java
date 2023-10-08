package greencity.dto.notificationsource;


import greencity.enums.NotificationSourceType;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class NotificationSourceDto {
    private Long id;
    private NotificationSourceType notificationSourceType;
    private String enName;
    private String uaName;
}

package greencity.dto.notification;


import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class NotificationDtoResponse {
    private String userName;
    private String action;
    private String title;
    private String creationDate;
    private Boolean isRead;
}
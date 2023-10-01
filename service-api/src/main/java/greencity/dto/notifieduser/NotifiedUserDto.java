package greencity.dto.notifieduser;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class NotifiedUserDto {
    private Long id;
    private Long notificationId;
    private Boolean isRead;
    private Long userId;
}

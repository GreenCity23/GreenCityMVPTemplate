package greencity.dto.notifieduser;
import greencity.dto.user.UserVO;
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
    private UserVO user;
}

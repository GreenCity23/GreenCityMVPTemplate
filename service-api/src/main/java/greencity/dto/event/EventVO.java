package greencity.dto.event;

import greencity.dto.user.UserVO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventVO {

    Long id;
    String title;
    String description;
    UserVO organizer;
    String titleImage;
}
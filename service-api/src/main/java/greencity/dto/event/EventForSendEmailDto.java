package greencity.dto.event;

import greencity.dto.user.AttendersEmailsDto;
import greencity.dto.user.PlaceAuthorDto;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
public class EventForSendEmailDto {
    private String title;
    private String description;
    private ZonedDateTime creationDate;
    private PlaceAuthorDto author;
    private String titleImage;
    private boolean open;
    private List<AttendersEmailsDto> attenders;
}

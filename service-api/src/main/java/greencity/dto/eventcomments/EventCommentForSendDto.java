package greencity.dto.eventcomments;

import greencity.dto.event.EventAuthorDto;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
public class EventCommentForSendDto {
    private String eventName;
    private EventAuthorDto eventAuthorDto;
    private EventCommentAuthorDto eventCommentAuthorDto;
    private LocalDateTime eventCommentCreationDate;
    private String eventCommentText;
    private String eventCommentLink;
}

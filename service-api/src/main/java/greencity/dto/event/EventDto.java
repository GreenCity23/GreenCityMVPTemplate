package greencity.dto.event;

import greencity.dto.tag.TagUaEnDto;
import greencity.dto.user.AttendersEmailsDto;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

/**
 * DTO for {greencity.entity.Event}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EventDto implements Serializable {
    private Long id;
    private String title;
    private String description;
    private ZonedDateTime creationDate;
    private List<EventDateLocationDto> dateLocations;
    private EventAuthorDto organizer;
    private String titleImage;
    private boolean open;
    private boolean isSubscribed;
    private boolean isFavorite;
    private Set<TagUaEnDto> tags;
    private List<String> additionalImages;
    private List<AttendersEmailsDto> attendersEmailsDtos;
}
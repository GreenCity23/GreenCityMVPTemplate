package greencity.dto.event;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
@Builder
public class EventDto implements Serializable {
    @NotNull
    Long id;
    @NotNull
    @Pattern(message = "The title should be at least 5 and not more than 70 characters long.", regexp = "^.{5,69}$")
    String title;
    @NotNull
    String description;
    ZonedDateTime creationDate;
    List<EventDateLocationDto> dateLocations;
    EventAuthorDto organizer;
    String titleImage;
    boolean open;
    boolean isSubscribed;
    boolean isFavorite;
    Set<TagUaEnDto> tags;
    List<String> additionalImages;
}
package greencity.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

/**
 * DTO for {greencity.entity.DateLocation}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDateLocationDto {
    Long id;
    @JsonIgnore
    EventDto event;
    ZonedDateTime startDate;
    ZonedDateTime finishDate;
    String onlineLink;
    AddressDto coordinates;
}
package greencity.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * DTO for {greencity.entity.DateLocation}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDateLocationDto implements Serializable {
    Long id;
    @JsonIgnore
    EventDto event;
    ZonedDateTime startDate;
    ZonedDateTime finishDate;
    String onlineLink;
    AddressDto coordinates;
}
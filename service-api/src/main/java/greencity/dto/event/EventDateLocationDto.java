package greencity.dto.event;

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
    @NotNull
    Long id;
    @NotNull
    ZonedDateTime startDate;
    @NotNull
    ZonedDateTime finishDate;
    @NotNull
    String onlineLink;
    @NotNull
    AddressDto coordinates;
}
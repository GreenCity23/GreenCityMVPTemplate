package greencity.dto.event;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * DTO for {greencity.entity.User}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class EventAuthorDto implements Serializable {
    @NotNull
    Long id;
    @NotEmpty
    String name;
    double organizerRating;
}
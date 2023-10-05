package greencity.dto.event;

import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * DTO for {greencity.entity.User}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class EventAttenderDto {
    @NotNull
    Long id;
    @NotNull
    String name;
    String imagePath;
}
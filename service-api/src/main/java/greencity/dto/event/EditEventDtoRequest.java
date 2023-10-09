package greencity.dto.event;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class EditEventDtoRequest {
    @NotNull
    Long id;
    private List<EventDateLocationDto> datesLocations;
    @NotEmpty
    @Size(min = 20, max = 63206)
    private String description;
    @NotEmpty
    private List<String> tags;
    @NotEmpty
    private String open;
    @NotEmpty
    @Size(min = 1, max = 70)
    private String title;
}

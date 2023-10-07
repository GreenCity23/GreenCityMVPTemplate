package greencity.dto.event;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class AddEventDtoRequest {

    private Long id;
    
    private List<EventDateLocationDto> datesLocations;

    @NotEmpty
    @Size(min = 20, max = 63206)
    private String description;

    private List<String> tags;

    @NotEmpty
    private String open;

    @NotEmpty
    @Size(min = 1, max = 70)
    private String title;
}

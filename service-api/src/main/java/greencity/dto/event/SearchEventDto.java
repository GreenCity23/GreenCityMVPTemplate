package greencity.dto.event;

import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@Setter
@Getter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SearchEventDto {
    private Long id;
    private String title;
    private EventAuthorDto author;
    private ZonedDateTime creationDate;
    private List<String> tags;
}

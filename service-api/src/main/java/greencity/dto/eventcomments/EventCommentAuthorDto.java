package greencity.dto.eventcomments;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class EventCommentAuthorDto {
    @NotEmpty
    private Long id;

    @NotEmpty
    private String name;

    private String userProfilePicturePath;
}


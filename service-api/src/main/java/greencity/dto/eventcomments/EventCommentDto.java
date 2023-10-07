package greencity.dto.eventcomments;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class EventCommentDto {
    @NotNull
    @Min(1)
    private Long id;

    private LocalDateTime createdDate;


    private LocalDateTime modifiedDate;

    private EventCommentAuthorDto author;

    private String text;

    private int replies;

    private int likes;

    private boolean currentUserLiked;
}
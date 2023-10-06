package greencity.dto.eventcomments;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AmountCommentLikesDto {
    private Long id;
    private Integer amountLikes;
    private Boolean liked;
    private Long userId;
}

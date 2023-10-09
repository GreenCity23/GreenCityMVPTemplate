package greencity.dto.achievement;


import greencity.dto.user.UserVO;
import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActionVO {
    private Long id;

    private UserVO user;

    private AchievementCategoryVO achievementCategory;

    private Integer count = 0;
}

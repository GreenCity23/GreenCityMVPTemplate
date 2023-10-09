package greencity.dto.achievement;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Getter
@Setter
public class AchievementNotification {
    private Long id;
    private String title;
    private String description;
    private String message;
}

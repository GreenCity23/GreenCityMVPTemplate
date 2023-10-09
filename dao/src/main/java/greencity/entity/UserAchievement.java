package greencity.entity;


import greencity.enums.AchievementStatus;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user_achievements")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserAchievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Achievement achievement;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AchievementStatus achievementStatus = AchievementStatus.INACTIVE;

    @Column
    private boolean notified;
}

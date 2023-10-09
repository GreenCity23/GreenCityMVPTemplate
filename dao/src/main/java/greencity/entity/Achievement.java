package greencity.entity;


import greencity.entity.localization.AchievementTranslation;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "achievements")
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = {CascadeType.ALL},
            mappedBy = "achievement")

    private List<AchievementTranslation> translations;

    @OneToMany(cascade = {CascadeType.ALL},
            mappedBy = "achievement")

    private List<UserAchievement> userAchievements;

    @ManyToOne
    private AchievementCategory achievementCategory;

    @Column(nullable = false)
    private Integer condition;


}

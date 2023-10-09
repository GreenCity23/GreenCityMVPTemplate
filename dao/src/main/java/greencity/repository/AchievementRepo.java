package greencity.repository;

import greencity.entity.Achievement;
import greencity.entity.UserAchievement;
import greencity.entity.localization.AchievementTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AchievementRepo extends JpaRepository<Achievement, Long> {
    /**
     * Retrieves the list of AchievementTranslations for a given user id.
     *
     * @param userId The id of the user.
     * @return The list of AchievementTranslations associated with the user's achievements.
     */
    @Query(value = "SELECT at FROM AchievementTranslation at WHERE at.achievement IN (SELECT ua.achievement FROM UserAchievement ua WHERE ua.user.id = :userId)")
    List<AchievementTranslation> findByUserAchievementsUserId(Long userId);

    /**
     * Retrieves the UserAchievement by the given userId and achievementId.
     *
     * @param userId The ID of the user.
     * @param achievementId The ID of the achievement.
     * @return The UserAchievement matching the userId and achievementId, or null if not found.
     */
    @Query(value = "FROM UserAchievement u WHERE u.user.id =:userId AND u.achievement.id =:achievementId")
    UserAchievement getUserAchievementByIdAndAchievementId(Long userId, Long achievementId);
}

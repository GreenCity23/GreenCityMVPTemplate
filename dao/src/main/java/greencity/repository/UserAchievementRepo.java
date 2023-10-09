package greencity.repository;

import greencity.entity.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAchievementRepo extends JpaRepository<UserAchievement, Long> {
}

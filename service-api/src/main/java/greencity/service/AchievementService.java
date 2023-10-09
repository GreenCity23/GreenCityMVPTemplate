package greencity.service;

import greencity.dto.achievement.AchievementNotification;
import greencity.dto.achievement.AchievementVO;

import java.util.List;

public interface AchievementService {
    List<AchievementVO> findAll();

    List<AchievementNotification> findActiveAchievements(Long userId);
}

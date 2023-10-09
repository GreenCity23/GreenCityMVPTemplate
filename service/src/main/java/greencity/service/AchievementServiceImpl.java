package greencity.service;

import greencity.dto.achievement.AchievementNotification;
import greencity.dto.achievement.AchievementVO;
import greencity.entity.Achievement;
import greencity.entity.UserAchievement;
import greencity.entity.localization.AchievementTranslation;
import greencity.repository.AchievementRepo;
import greencity.repository.UserAchievementRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AchievementServiceImpl implements AchievementService {
    AchievementRepo achievementRepo;
    UserAchievementRepo userAchievementRepo;
    ModelMapper modelMapper;

    public AchievementServiceImpl(AchievementRepo achievementRepo, UserAchievementRepo userAchievementRepo, ModelMapper modelMapper) {
        this.achievementRepo = achievementRepo;
        this.userAchievementRepo = userAchievementRepo;
        this.modelMapper = modelMapper;
    }

    /**
     * Retrieves a list of all achievement value objects.
     *
     * @return a list of AchievementVO objects representing all achievements in the system.
     */
    @Override
    public List<AchievementVO> findAll() {
        List<Achievement> achievements = achievementRepo.findAll();
        return achievements.stream().map(achievement -> modelMapper.map(achievement, AchievementVO.class)).collect(Collectors.toList());
    }

    /**
     * Finds active achievements for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of active achievement notifications
     */
    @Override
    public List<AchievementNotification> findActiveAchievements(Long userId) {
        List<AchievementNotification> achievementNotifications = new ArrayList<>();
        List<AchievementTranslation> achievements = achievementRepo.findByUserAchievementsUserId(userId);
        achievements.forEach(achievement -> {
            AchievementNotification notification = AchievementNotification.builder()
                    .id(achievement.getId())
                    .description(achievement.getDescription())
                    .title(achievement.getTitle())
                    .message(achievement.getMessage())
                    .build();
            achievementNotifications.add(notification);
            UserAchievement userAchievement = achievementRepo.getUserAchievementByIdAndAchievementId(userId, achievement.getAchievement().getId());
            userAchievement.setNotified(true);
            userAchievementRepo.save(userAchievement);
        });
        return achievementNotifications;
    }
}

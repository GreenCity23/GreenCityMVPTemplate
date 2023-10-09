package greencity.mapping;

import greencity.dto.achievement.AchievementCategoryVO;
import greencity.dto.achievement.AchievementTranslationVO;
import greencity.dto.achievement.AchievementVO;
import greencity.dto.language.LanguageVO;
import greencity.entity.Achievement;
import greencity.entity.localization.AchievementTranslation;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AchievementVOMapper extends AbstractConverter<Achievement, AchievementVO> {
    @Override
    protected AchievementVO convert(Achievement achievement) {
        List<AchievementTranslationVO> translations = mapAchievementTranslations(achievement);
        AchievementCategoryVO category = mapAchievementCategory(achievement);

        return AchievementVO.builder()
                .id(achievement.getId())
                .translations(translations)
                .achievementCategory(category)
                .condition(achievement.getCondition())
                .build();
    }

    private List<AchievementTranslationVO> mapAchievementTranslations(Achievement achievement) {
        return achievement.getTranslations().stream()
                .map(translation -> AchievementTranslationVO.builder()
                        .id(translation.getId())
                        .title(translation.getTitle())
                        .description(translation.getDescription())
                        .message(translation.getMessage())
                        .language(mapLanguage(translation))
                        .build())
                .collect(Collectors.toList());
    }

    private LanguageVO mapLanguage(AchievementTranslation translation) {
        return LanguageVO.builder()
                .id(translation.getLanguage().getId())
                .code(translation.getLanguage().getCode())
                .build();
    }

    private AchievementCategoryVO mapAchievementCategory(Achievement achievement) {
        return AchievementCategoryVO.builder()
                .id(achievement.getAchievementCategory().getId())
                .name(achievement.getAchievementCategory().getName())
                .build();
    }
}
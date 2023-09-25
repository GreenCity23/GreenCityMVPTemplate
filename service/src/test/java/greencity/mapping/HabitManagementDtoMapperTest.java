package greencity.mapping;

import greencity.ModelUtils;
import greencity.dto.habit.HabitManagementDto;
import greencity.dto.habittranslation.HabitTranslationManagementDto;
import greencity.entity.Habit;
import greencity.entity.HabitTranslation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class HabitManagementDtoMapperTest {
    @InjectMocks
    private HabitManagementDtoMapper habitManagementDtoMapper;

    @Test
    void testConvert() {
        Habit habit = Habit.builder()
            .id(1L)
            .image("Image")
            .complexity(0)
            .defaultDuration(0)
            .isCustomHabit(true)
            .userId(1L)
            .tags(new HashSet<>(ModelUtils.getHabitsTags()))
            .shoppingListItems(new HashSet<>(Collections.singleton(ModelUtils.getShoppingListItem())))
            .habitTranslations(Collections.singletonList(HabitTranslation.builder()
                .id(1L)
                .name("Name")
                .description("Description")
                .habitItem("Item")
                .language(ModelUtils.getLanguage())
                .build()))
            .build();

        HabitManagementDto expected = HabitManagementDto.builder()
            .id(habit.getId())
            .image(habit.getImage())
            .complexity(habit.getComplexity())
            .defaultDuration(habit.getDefaultDuration())
            .habitTranslations(habit.getHabitTranslations()
                .stream().map(habitTranslation -> HabitTranslationManagementDto.builder()
                    .id(habitTranslation.getId())
                    .description(habitTranslation.getDescription())
                    .habitItem(habitTranslation.getHabitItem())
                    .name(habitTranslation.getName())
                    .languageCode(habitTranslation.getLanguage().getCode())
                    .build())
                .collect(Collectors.toList()))
            .build();

        assertEquals(expected, habitManagementDtoMapper.convert(habit));
    }
}

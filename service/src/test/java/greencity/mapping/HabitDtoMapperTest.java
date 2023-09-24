package greencity.mapping;

import greencity.ModelUtils;
import greencity.dto.habit.HabitDto;
import greencity.dto.habittranslation.HabitTranslationDto;
import greencity.dto.shoppinglistitem.ShoppingListItemDto;
import greencity.entity.Habit;
import greencity.entity.HabitTranslation;
import greencity.entity.localization.ShoppingListItemTranslation;
import greencity.entity.localization.TagTranslation;
import greencity.enums.ShoppingListItemStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class HabitDtoMapperTest {
    @InjectMocks
    private HabitDtoMapper habitDtoMapper;

    @Test
    void testConvert() {
        HabitTranslation habitTranslation = HabitTranslation.builder()
            .id(1L)
            .name("Name")
            .description("Description")
            .habitItem("Item")
            .language(ModelUtils.getLanguage())
            .habit(Habit.builder()
                .id(1L)
                .image("Image")
                .complexity(0)
                .defaultDuration(0)
                .isCustomHabit(true)
                .userId(1L)
                .tags(new HashSet<>(ModelUtils.getHabitsTags()))
                .shoppingListItems(new HashSet<>(Collections.singleton(ModelUtils.getShoppingListItem())))
                .build())
            .build();

        HabitDto expected = HabitDto.builder()
            .id(habitTranslation.getHabit().getId())
            .image(habitTranslation.getHabit().getImage())
            .defaultDuration(habitTranslation.getHabit().getDefaultDuration())
            .complexity(habitTranslation.getHabit().getComplexity())
            .habitTranslation(HabitTranslationDto.builder()
                .description(habitTranslation.getDescription())
                .habitItem(habitTranslation.getHabitItem())
                .name(habitTranslation.getName())
                .languageCode(habitTranslation.getLanguage().getCode())
                .build())
            .tags(habitTranslation.getHabit().getTags().stream()
                .flatMap(tag -> tag.getTagTranslations().stream())
                .filter(tagTranslation -> tagTranslation.getLanguage().equals(habitTranslation.getLanguage()))
                .map(TagTranslation::getName).collect(Collectors.toList()))
            .shoppingListItems(habitTranslation.getHabit().getShoppingListItems() != null
                ? habitTranslation.getHabit().getShoppingListItems().stream()
                    .map(shoppingListItem -> ShoppingListItemDto.builder()
                        .id(shoppingListItem.getId())
                        .status(ShoppingListItemStatus.ACTIVE.toString())
                        .text(shoppingListItem.getTranslations().stream()
                            .filter(shoppingListItemTranslation -> shoppingListItemTranslation
                                .getLanguage().equals(habitTranslation.getLanguage()))
                            .map(ShoppingListItemTranslation::getContent)
                            .findFirst().orElse(null))
                        .build())
                    .collect(Collectors.toList())
                : new ArrayList<>())
            .build();

        assertEquals(expected, habitDtoMapper.convert(habitTranslation));
    }
}

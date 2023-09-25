package greencity.mapping;

import static org.junit.jupiter.api.Assertions.assertEquals;

import greencity.dto.habittranslation.HabitTranslationDto;
import greencity.entity.HabitTranslation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class HabitTranslationMapperTest {
    private HabitTranslationMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new HabitTranslationMapper();
    }

    @Test
    void convertTest() {
        HabitTranslationDto habitTranslationDto = HabitTranslationDto.builder()
            .name("Test Habit")
            .description("This is a test habit.")
            .habitItem("Test Item")
            .build();

        HabitTranslation habitTranslation = mapper.convert(habitTranslationDto);

        assertEquals(habitTranslationDto.getName(), habitTranslation.getName());
        assertEquals(habitTranslationDto.getDescription(), habitTranslation.getDescription());
        assertEquals(habitTranslationDto.getHabitItem(), habitTranslation.getHabitItem());
    }

    @Test
    void mapAllToListTest() {
        List<HabitTranslationDto> dtoList = new ArrayList<>();
        dtoList.add(HabitTranslationDto.builder()
            .name("Test Habit 1")
            .description("This is a test habit 1.")
            .habitItem("Test Item 1")
            .build());
        dtoList.add(HabitTranslationDto.builder()
            .name("Test Habit 2")
            .description("This is a test habit 2.")
            .habitItem("Test Item 2")
            .build());

        List<HabitTranslation> translationList = mapper.mapAllToList(dtoList);

        assertEquals(dtoList.size(), translationList.size());

        for (int i = 0; i < dtoList.size(); i++) {
            HabitTranslationDto dto = dtoList.get(i);
            HabitTranslation translation = translationList.get(i);
            assertEquals(dto.getName(), translation.getName());
            assertEquals(dto.getDescription(), translation.getDescription());
            assertEquals(dto.getHabitItem(), translation.getHabitItem());
        }
    }
}

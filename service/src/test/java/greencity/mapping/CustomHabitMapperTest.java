package greencity.mapping;

import greencity.dto.habit.AddCustomHabitDtoRequest;
import greencity.entity.Habit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomHabitMapperTest {
    CustomHabitMapper customHabitMapper;

    @BeforeEach
    public void setUp() {
        customHabitMapper = new CustomHabitMapper();
    }

    @Test
    @DisplayName("AddCustomHabitDtoRequest convert to Habit test")

    void testConvert() {
        AddCustomHabitDtoRequest addCustomHabitDtoRequest =
            new AddCustomHabitDtoRequest().setImage("Image").setComplexity(2).setDefaultDuration(30);

        Habit habit = customHabitMapper.convert(addCustomHabitDtoRequest);

        assertEquals("Image", habit.getImage());
        assertEquals(2, habit.getComplexity());
        assertEquals(30, habit.getDefaultDuration());
    }

}
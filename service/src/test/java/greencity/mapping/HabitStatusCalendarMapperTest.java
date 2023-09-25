package greencity.mapping;

import static org.junit.jupiter.api.Assertions.assertEquals;

import greencity.dto.habit.HabitAssignVO;
import greencity.dto.habitstatuscalendar.HabitStatusCalendarVO;
import greencity.entity.HabitStatusCalendar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class HabitStatusCalendarMapperTest {
    private HabitStatusCalendarMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new HabitStatusCalendarMapper();
    }

    @Test
    void convertTest() {
        HabitStatusCalendarVO habitStatusCalendarVO = HabitStatusCalendarVO.builder()
            .id(1L)
            .enrollDate(LocalDate.of(2023, 9, 12))
            .habitAssignVO(HabitAssignVO.builder().id(2L).build())
            .build();

        HabitStatusCalendar habitStatusCalendar = mapper.convert(habitStatusCalendarVO);

        assertEquals(habitStatusCalendarVO.getId(), habitStatusCalendar.getId());
        assertEquals(habitStatusCalendarVO.getEnrollDate(), habitStatusCalendar.getEnrollDate());

        HabitAssignVO habitAssignVO = habitStatusCalendarVO.getHabitAssignVO();
        assertEquals(habitAssignVO.getId(), habitStatusCalendar.getHabitAssign().getId());
    }
}

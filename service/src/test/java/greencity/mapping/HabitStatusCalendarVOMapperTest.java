package greencity.mapping;

import static org.junit.jupiter.api.Assertions.assertEquals;

import greencity.dto.habit.HabitAssignVO;
import greencity.dto.habitstatuscalendar.HabitStatusCalendarVO;
import greencity.entity.HabitAssign;
import greencity.entity.HabitStatusCalendar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class HabitStatusCalendarVOMapperTest {
    private HabitStatusCalendarVOMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new HabitStatusCalendarVOMapper();
    }

    @Test
    void convertTest() {
        HabitStatusCalendar habitStatusCalendar = HabitStatusCalendar.builder()
            .id(1L)
            .enrollDate(LocalDate.of(2023, 9, 12))
            .habitAssign(HabitAssign.builder().id(2L).build())
            .build();

        HabitStatusCalendarVO habitStatusCalendarVO = mapper.convert(habitStatusCalendar);

        assertEquals(habitStatusCalendar.getId(), habitStatusCalendarVO.getId());
        assertEquals(habitStatusCalendar.getEnrollDate(), habitStatusCalendarVO.getEnrollDate());

        HabitAssign habitAssign = habitStatusCalendar.getHabitAssign();
        HabitAssignVO habitAssignVO = habitStatusCalendarVO.getHabitAssignVO();

        assertEquals(habitAssign.getId(), habitAssignVO.getId());
    }
}

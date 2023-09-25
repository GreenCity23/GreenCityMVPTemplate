package greencity.mapping;

import greencity.dto.habit.HabitAssignDto;
import greencity.dto.habitstatuscalendar.HabitStatusCalendarDto;
import greencity.entity.HabitAssign;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Collectors;

import static greencity.ModelUtils.getHabitAssign;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class HabitAssignDtoMapperTest {
    @InjectMocks
    private HabitAssignDtoMapper mapper;

    @Test
    void convertTest() {
        HabitAssign habitAssign = getHabitAssign();
        HabitAssignDto expected = HabitAssignDto.builder()
            .id(habitAssign.getId())
            .status(habitAssign.getStatus())
            .createDateTime(habitAssign.getCreateDate())
            .userId(habitAssign.getUser().getId())
            .duration(habitAssign.getDuration())
            .habitStreak(habitAssign.getHabitStreak())
            .workingDays(habitAssign.getWorkingDays())
            .lastEnrollmentDate(habitAssign.getLastEnrollmentDate())
            .habitStatusCalendarDtoList(habitAssign.getHabitStatusCalendars().stream().map(
                habitStatusCalendar -> HabitStatusCalendarDto.builder()
                    .id(habitStatusCalendar.getId())
                    .enrollDate(habitStatusCalendar.getEnrollDate())
                    .build())
                .collect(Collectors.toList()))
            .build();

        assertEquals(expected, mapper.convert(habitAssign));
    }
}
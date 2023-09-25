package greencity.mapping;

import greencity.dto.habit.HabitAssignManagementDto;
import greencity.entity.HabitAssign;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static greencity.ModelUtils.getHabitAssign;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class HabitAssignManagementDtoMapperTest {
    @InjectMocks
    private HabitAssignManagementDtoMapper mapper;

    @Test
    void convertTest() {
        HabitAssign habitAssign = getHabitAssign();

        HabitAssignManagementDto expected = HabitAssignManagementDto.builder()
            .id(habitAssign.getId())
            .status(habitAssign.getStatus())
            .createDateTime(habitAssign.getCreateDate())
            .userId(habitAssign.getUser().getId())
            .habitId(habitAssign.getHabit().getId())
            .duration(habitAssign.getDuration())
            .habitStreak(habitAssign.getHabitStreak())
            .workingDays(habitAssign.getWorkingDays())
            .lastEnrollment(habitAssign.getLastEnrollmentDate())
            .build();

        assertEquals(expected, mapper.convert(habitAssign));
    }
}
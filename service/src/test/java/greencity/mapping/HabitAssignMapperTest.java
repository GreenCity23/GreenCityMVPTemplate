package greencity.mapping;

import greencity.dto.habit.HabitAssignDto;
import greencity.dto.habit.HabitDto;
import greencity.dto.habitstatuscalendar.HabitStatusCalendarDto;
import greencity.dto.user.UserShoppingListItemAdvanceDto;
import greencity.entity.HabitAssign;
import greencity.entity.UserShoppingListItem;
import greencity.enums.ShoppingListItemStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static greencity.ModelUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class HabitAssignMapperTest {
    @InjectMocks
    private HabitAssignMapper mapper;

    @Test
    void convertTest() {
        HabitAssign habitAssign = getHabitAssign();
        List<UserShoppingListItem> userShoppingListItems = new ArrayList<>();
        userShoppingListItems.add(getUserShoppingListItem());
        userShoppingListItems.get(0).setStatus(ShoppingListItemStatus.INPROGRESS);
        habitAssign.setUserShoppingListItems(userShoppingListItems);

        HabitAssignDto habitAssignDto = HabitAssignDto.builder()
            .id(habitAssign.getId())
            .status(habitAssign.getStatus())
            .habit(HabitDto.builder()
                .id(habitAssign.getHabit().getId())
                .complexity(habitAssign.getHabit().getComplexity())
                .defaultDuration(habitAssign.getHabit().getDefaultDuration())
                .build())
            .createDateTime(habitAssign.getCreateDate())
            .duration(habitAssign.getDuration())
            .userId(habitAssign.getUser().getId())
            .habitStreak(habitAssign.getHabitStreak())
            .workingDays(habitAssign.getWorkingDays())
            .lastEnrollmentDate(habitAssign.getLastEnrollmentDate())
            .userShoppingListItems(userShoppingListItems
                .stream().map(userShoppingListItem -> UserShoppingListItemAdvanceDto.builder()
                    .id(userShoppingListItem.getId())
                    .shoppingListItemId(userShoppingListItem.getShoppingListItem().getId())
                    .status(userShoppingListItem.getStatus())
                    .dateCompleted(userShoppingListItem.getDateCompleted())
                    .build())
                .collect(Collectors.toList()))
            .habitStatusCalendarDtoList(habitAssign.getHabitStatusCalendars().stream().map(
                habitStatusCalendar -> HabitStatusCalendarDto.builder()
                    .id(habitStatusCalendar.getId())
                    .enrollDate(habitStatusCalendar.getEnrollDate())
                    .build())
                .collect(Collectors.toList()))
            .build();
        HabitAssign habitAssignConverted = mapper.convert(habitAssignDto);

        assertEquals(habitAssign.getUserShoppingListItems().get(0).getId(),
            habitAssignConverted.getUserShoppingListItems().get(0).getId());
        assertEquals(habitAssign.getUserShoppingListItems().get(0).getStatus(),
            habitAssignConverted.getUserShoppingListItems().get(0).getStatus());
        assertEquals(habitAssign.getHabit().getId(), habitAssignConverted.getHabit().getId());
        assertEquals(habitAssign.getHabit().getComplexity(), habitAssignConverted.getHabit().getComplexity());
        assertEquals(habitAssign.getHabitStreak(), habitAssignConverted.getHabitStreak());
        assertEquals(habitAssign.getCreateDate(), habitAssignConverted.getCreateDate());
        assertEquals(habitAssign.getWorkingDays(), habitAssignConverted.getWorkingDays());
        assertEquals(habitAssign.getDuration(), habitAssignConverted.getDuration());
        assertEquals(habitAssign.getLastEnrollmentDate(), habitAssignConverted.getLastEnrollmentDate());
    }
}
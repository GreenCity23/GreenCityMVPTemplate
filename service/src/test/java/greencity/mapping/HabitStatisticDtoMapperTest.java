package greencity.mapping;

import greencity.ModelUtils;
import greencity.dto.habitstatistic.HabitStatisticDto;
import greencity.entity.HabitStatistic;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class HabitStatisticDtoMapperTest {

    @InjectMocks
    private HabitStatisticDtoMapper habitStatisticDtoMapper;

    @Test
    void testConvert() {
        HabitStatistic habitStatistic = HabitStatistic.builder()
            .id(1L)
            .habitRate(ModelUtils.getHabitStatistic().getHabitRate())
            .createDate(ModelUtils.zonedDateTime)
            .amountOfItems(0)
            .habitAssign(ModelUtils.getHabitAssign())
            .build();

        HabitStatisticDto expected = HabitStatisticDto.builder()
            .id(habitStatistic.getId())
            .amountOfItems(habitStatistic.getAmountOfItems())
            .createDate(habitStatistic.getCreateDate())
            .habitRate(habitStatistic.getHabitRate())
            .habitAssignId(habitStatistic.getHabitAssign().getId())
            .build();

        assertEquals(expected, habitStatisticDtoMapper.convert(habitStatistic));
    }
}

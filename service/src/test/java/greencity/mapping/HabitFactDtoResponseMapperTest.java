package greencity.mapping;

import greencity.ModelUtils;
import greencity.dto.habit.HabitVO;
import greencity.dto.habitfact.HabitFactDtoResponse;
import greencity.dto.habitfact.HabitFactTranslationDto;
import greencity.dto.habitfact.HabitFactVO;
import greencity.dto.language.LanguageDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class HabitFactDtoResponseMapperTest {

    @InjectMocks
    private HabitFactDtoResponseMapper habitFactDtoResponseMapper;

    @Test
    void testConvert() {
        HabitFactVO habitFactVO = HabitFactVO.builder()
            .id(1L)
            .translations(Collections.singletonList(ModelUtils.getFactTranslationVO()))
            .habit(HabitVO.builder()
                .id(1L)
                .image("Image")
                .complexity(0)
                .build())
            .build();

        HabitFactDtoResponse expected = HabitFactDtoResponse.builder()
            .id(habitFactVO.getId())
            .habit(habitFactVO.getHabit())
            .translations(habitFactVO.getTranslations().stream().map(
                habitFactTranslationVO -> HabitFactTranslationDto.builder()
                    .id(habitFactTranslationVO.getId())
                    .content(habitFactTranslationVO.getContent())
                    .factOfDayStatus(habitFactTranslationVO.getFactOfDayStatus())
                    .language(LanguageDTO.builder()
                        .id(habitFactTranslationVO.getLanguage().getId())
                        .code(habitFactTranslationVO.getLanguage().getCode())
                        .build())
                    .build())
                .collect(Collectors.toList()))
            .build();

        assertEquals(expected, habitFactDtoResponseMapper.convert(habitFactVO));
    }
}

package greencity.mapping;

import static org.junit.jupiter.api.Assertions.assertEquals;

import greencity.dto.language.LanguageTranslationDTO;
import greencity.entity.HabitFactTranslation;
import greencity.entity.Language;
import greencity.enums.FactOfDayStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LanguageTranslationDtoMapperTest {
    private LanguageTranslationDtoMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new LanguageTranslationDtoMapper();
    }

    @Test
    void convertTest() {
        FactOfDayStatus factOfDayStatus = FactOfDayStatus.CURRENT;
        HabitFactTranslation habitFactTranslation = HabitFactTranslation.builder()
            .content("This is a test translation.")
            .factOfDayStatus(factOfDayStatus)
            .language(Language.builder()
                .id(1L)
                .code("en")
                .build())
            .build();

        LanguageTranslationDTO translationDTO = mapper.convert(habitFactTranslation);

        assertEquals(habitFactTranslation.getContent(), translationDTO.getContent());
        assertEquals(habitFactTranslation.getLanguage().getId(), translationDTO.getLanguage().getId());
        assertEquals(habitFactTranslation.getLanguage().getCode(), translationDTO.getLanguage().getCode());
    }
}

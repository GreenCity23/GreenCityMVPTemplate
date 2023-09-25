package greencity.mapping;

import greencity.dto.tag.NewTagDto;
import greencity.entity.Tag;
import greencity.entity.localization.TagTranslation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static greencity.ModelUtils.getTag;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class NewTagDtoMapperTest {
    @InjectMocks
    private NewTagDtoMapper newTagDtoMapper;

    @Test
    void convertTest() {
        Tag source = getTag();
        NewTagDto newTagDto = newTagDtoMapper.convert(source);

        String expectedName = source.getTagTranslations().stream()
            .filter(tagTranslation -> tagTranslation.getLanguage().getCode().equals("en"))
            .map(TagTranslation::getName)
            .findFirst()
            .orElse("");

        String expectedNameUa = source.getTagTranslations().stream()
            .filter(tagTranslation -> tagTranslation.getLanguage().getCode().equals("ua"))
            .map(TagTranslation::getName)
            .findFirst()
            .orElse("");

        assertEquals(expectedName, newTagDto.getName());
        assertEquals(expectedNameUa, newTagDto.getNameUa());
        assertEquals(source.getId(), newTagDto.getId());
    }
}

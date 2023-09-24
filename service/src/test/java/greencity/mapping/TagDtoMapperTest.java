package greencity.mapping;

import greencity.dto.tag.TagDto;
import greencity.entity.Tag;
import greencity.entity.localization.TagTranslation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static greencity.ModelUtils.getTagTranslations;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Arthur Mkrtchian
 */
class TagDtoMapperTest {

    TagDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TagDtoMapper();
    }

    @Test
    @DisplayName("Convert TagTranslation to TagDto test")
    void convert() {
        TagTranslation tagTranslation = getTagTranslations().get(1).setTag(new Tag().setId(1L));

        TagDto expected = TagDto.builder()
            .id(1L)
            .name("News")
            .build();

        TagDto actual = mapper.convert(tagTranslation);

        assertEquals(expected, actual);
    }
}
package greencity.mapping;

import greencity.ModelUtils;
import greencity.dto.language.LanguageVO;
import greencity.dto.tag.TagTranslationVO;
import greencity.dto.tag.TagVO;
import greencity.entity.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Arthur Mkrtchian
 */
class TagVOMapperTest {

    TagVOMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TagVOMapper();
    }

    @Test
    @DisplayName("Convert Tag to TagVO test")
    void convert() {
        Tag tag = ModelUtils.getTag();

        TagVO expectedTagVO = TagVO.builder()
            .id(tag.getId())
            .type(tag.getType())
            .tagTranslations(tag.getTagTranslations().stream()
                .map(tagTranslation -> TagTranslationVO.builder()
                    .id(tagTranslation.getId())
                    .name(tagTranslation.getName())
                    .languageVO(LanguageVO.builder()
                        .id(tagTranslation.getLanguage().getId())
                        .code(tagTranslation.getLanguage().getCode())
                        .build())
                    .build())
                .collect(Collectors.toList()))
            .build();

        TagVO actual = mapper.convert(tag);

        assertEquals(expectedTagVO, actual);
    }
}
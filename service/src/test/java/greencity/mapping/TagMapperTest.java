package greencity.mapping;

import greencity.dto.tag.TagVO;
import greencity.entity.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static greencity.ModelUtils.getTag;
import static greencity.ModelUtils.getTagVO;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Arthur Mkrtchian
 */
class TagMapperTest {

    TagMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TagMapper();
    }

    @Test
    @DisplayName("Convert TagVO to Tag test")
    void convert() {
        TagVO tagVO = getTagVO()
            .setEcoNews(Collections.emptyList())
            .setHabits(Collections.emptySet());

        Tag expected = getTag();

        Tag actual = mapper.convert(tagVO);
        assertEquals(expected, actual);
    }
}
package greencity.validator;

import greencity.dto.econews.AddEcoNewsDtoRequest;
import greencity.exception.exceptions.InvalidURLException;
import greencity.exception.exceptions.WrongCountOfTagsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static greencity.ModelUtils.getAddEcoNewsDtoRequest;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
class EcoNewsDtoRequestValidatorTest {
    @InjectMocks
    private EcoNewsDtoRequestValidator validator;

    @Test
    void isValidTrueTest() {
        AddEcoNewsDtoRequest request = getAddEcoNewsDtoRequest();
        request.setSource("https://eco-lavca.ua/");
        assertTrue(validator.isValid(request, null));
    }

    @Test
    void testInvalidSourceUrl() {
        AddEcoNewsDtoRequest requestWithInvalidUrl = getAddEcoNewsDtoRequest();
        requestWithInvalidUrl.setSource("invalidurl");
        assertThrows(InvalidURLException.class, () -> validator.isValid(requestWithInvalidUrl, null));
    }

    @Test()
    void testEmptyTags() {
        AddEcoNewsDtoRequest requestWithNullTags = getAddEcoNewsDtoRequest();
        requestWithNullTags.setTags(Collections.emptyList());
        assertThrows(WrongCountOfTagsException.class, () -> validator.isValid(requestWithNullTags, null));
    }

    @Test
    void testTooManyTags() {
        AddEcoNewsDtoRequest requestWithNullTags = getAddEcoNewsDtoRequest();
        requestWithNullTags.setTags(Collections.nCopies(4, "Tag"));
        assertThrows(WrongCountOfTagsException.class, () -> validator.isValid(requestWithNullTags, null));
    }
}

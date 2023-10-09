package greencity.validator;

import greencity.service.LanguageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Locale;

@ExtendWith(SpringExtension.class)
class LanguageValidatorTest {
    @InjectMocks
    LanguageValidator languageValidator;
    @Mock
    private LanguageService languageService;

    @BeforeEach
    void setUp() {
        List<String> codes = List.of("ua", "en", "ru");
        Mockito.doReturn(codes).when(languageService).findAllLanguageCodes();

        languageValidator.initialize(null);
    }

    @DisplayName("Valid Language Test")
    @Test
    void isValidTrueLanguageTest() {
        Locale testLocaleEn = new Locale("en");
        Locale testLocaleUa = new Locale("ua");
        Locale testLocaleRu = new Locale("ru");
        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);

        Assertions.assertTrue(languageValidator.isValid(testLocaleEn, context));
        Assertions.assertTrue(languageValidator.isValid(testLocaleUa, context));
        Assertions.assertTrue(languageValidator.isValid(testLocaleRu, context));
    }

    @DisplayName("Invalid Language Test")
    @Test
    void isValidFalseLanguageTest() {
        Locale testLocaleDe = new Locale("de");
        Locale testLocaleFr = new Locale("fr");
        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);

        Assertions.assertFalse(languageValidator.isValid(testLocaleDe, context));
        Assertions.assertFalse(languageValidator.isValid(testLocaleFr, context));
    }
}

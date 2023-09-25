package greencity.validator;

import greencity.annotations.ValidLanguage;
import greencity.service.LanguageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Locale;

@Slf4j
public class LanguageValidator implements ConstraintValidator<ValidLanguage, Locale> {
    private List<String> codes;
    @Autowired
    private LanguageService languageService;

    @Override
    public void initialize(ValidLanguage constraintAnnotation) {
        try {
            codes = languageService.findAllLanguageCodes();
        } catch (Exception e) {
            log.warn("Occurred error during processing request: {}", e.getMessage());
            codes = List.of("ua", "en", "ru");
        }
    }

    @Override
    public boolean isValid(Locale value, ConstraintValidatorContext context) {
        return codes.contains(value.getLanguage());
    }
}

package greencity.validator;

import greencity.annotations.ImageValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ImageValidatorTest {
    private ImageValidator imageValidator;

    @Mock
    private ImageValidation imageValidation;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @BeforeEach
    public void setUp() {
        try (AutoCloseable ignored = MockitoAnnotations.openMocks(this)) {
            imageValidator = new ImageValidator();
            imageValidator.initialize(imageValidation);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void testValidImageType() {
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        assertTrue(imageValidator.isValid(multipartFile, constraintValidatorContext));
    }

    @Test
    void testValidImageType_PNG() {
        when(multipartFile.getContentType()).thenReturn("image/png");
        assertTrue(imageValidator.isValid(multipartFile, constraintValidatorContext));
    }

    @Test
    void testInvalidImageType() {
        when(multipartFile.getContentType()).thenReturn("application/pdf");
        assertFalse(imageValidator.isValid(multipartFile, constraintValidatorContext));
    }

    @Test
    void testNullImage() {
        assertTrue(imageValidator.isValid(null, constraintValidatorContext));
    }
}

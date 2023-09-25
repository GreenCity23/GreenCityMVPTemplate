package greencity.mapping;

import greencity.exception.exceptions.NotSavedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MultipartBase64ImageMapperTest {
    @InjectMocks
    private MultipartBase64ImageMapper multipartBase64ImageMapper;

//    @Test
//    void validConvertTest() {
//        String validBase64Image = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8" +
//            "/9hAAABzklEQVR4XqWTz0sCURDHu/ZHdOovqUunIq1LkFp5KM099MvIjMyK9lDniiiyDv4IVJJSoS2QoK" +
//            "KIOrgEQphgQS7IQtGhlul9V962WTcHht19+53Pm5k3r4mImhpx46Wrb6e5rXOjxRc8tk7OpSSnEFN7HXs" +
//            "afMgTUyf8KWmG/YMG2l8ALHRYt1qF6aQ4OBqVl1dPlP34LW1HrwnP85snml/JKg5XRBa8CRFaDtEBoAre" +
//            "pOgUDkprmzmKZ/KUzRWoUv2gYlkl2PPru77GICXPVEJEjAFAag53RMZOCIanpAfdOeBLIzq9eKQz5nZXW" +
//            "EaMAUB9SBEiDuCuvn3qADzxfSe/kH8po4zPHkoGAE3C7lxUH8wzyBcq+jq0A+4oUqsBeuwhrVCs6kLUCa" +
//            "E5mBtKAABaa/8uQ5oAaBIMgbyB9QYA+gKAxQxgR6eXYDZ8c+dwGMBYY6fxUwKaGGBNNFT00zTuV/dl499" +
//            "04EgZ85mayI7EgiExZ1EPgKOR0NhG2DEuHFsMQG2QEiKbhZIZglp5MN714OHw30Hio+xhEAwJuw8KxBga" +
//            "jPP6/iX5gmkFOyP4zyhzCKhIDUOCc7baQhq6jYaxtZPZxXR3+3+XqRH/BkJN9d+sB8gsAAAAAElFTkSuQmCC";
//
//        MultipartFile multipartFile = multipartBase64ImageMapper.convert(validBase64Image);
//
//        assertNotNull(multipartFile);
//        assertEquals("tempImage.jpg", multipartFile.getOriginalFilename());
//        assertEquals("image/jpeg", multipartFile.getContentType());
//        assertEquals("mainFile", multipartFile.getName());
//    }

//    @Test
//    void invalidConvertTest() {
//        String invalidBase64Image = "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAWklEQVR42mP8";
//
//        assertThrows(NotSavedException.class, () -> multipartBase64ImageMapper.convert(invalidBase64Image));
//    }
}

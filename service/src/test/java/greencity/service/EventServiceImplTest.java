package greencity.service;

import greencity.ModelUtils;
import greencity.client.RestClient;
import greencity.repository.EventRepo;
import greencity.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@ExtendWith(SpringExtension.class)
public class EventServiceImplTest {
    @Mock
    FileService fileService;
    @Mock
    ModelMapper modelMapper;
    @Mock
    RestClient restClient;
    @Mock
    HttpServletRequest httpServletRequest;
    @Mock
    private EventRepo eventRepo;
    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void save() {
        MultipartFile multipartFile = ModelUtils.getFile();
    }
}

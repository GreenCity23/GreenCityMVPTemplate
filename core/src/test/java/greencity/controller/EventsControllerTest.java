package greencity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import greencity.converters.UserArgumentResolver;
import greencity.dto.event.AddEventDtoRequest;
import greencity.dto.event.EventDto;
import greencity.dto.user.UserVO;
import greencity.exception.handler.CustomExceptionHandler;
import greencity.service.EventService;
import greencity.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

import static greencity.ModelUtils.getPrincipal;
import static greencity.ModelUtils.getUserVO;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EventsControllerTest {
    private static final String eventsLink = "/events";
    private MockMvc mockMvc;
    @InjectMocks
    private EventsController eventsController;
    @Mock
    private EventService eventService;
    @Mock
    private UserService userService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private ObjectMapper objectMapper;
    private Principal principal = getPrincipal();
    private ErrorAttributes errorAttributes = new DefaultErrorAttributes();


    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(eventsController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver(),
                        new UserArgumentResolver(userService, modelMapper))
                .setControllerAdvice(new CustomExceptionHandler(errorAttributes, objectMapper))
                .build();
    }


    @Test
    void createEvent() throws Exception {
        UserVO userVO = mock(UserVO.class);
        when(userVO.getId()).thenReturn(3L);

        String json = "{\"title\":\"test events\",\n" +
                      "\"description\":\"test test test test test test test test test\",\n" +
                      "\"open\":\"true\",\n" +
                      "\"datesLocations\":[\n" +
                      "\t{\n" +
                      "\t\"startDate\":\"2023-10-27T15:00:00Z\",\n" +
                      "\t\"finishDate\":\"2023-10-27T17:00:00Z\",\n" +
                      "\t\"coordinates\":{\n" +
                      "\t\t\"latitude\":1,\n" +
                      "\t\t\"longitude\":1\n" +
                      "\t    }\n" +
                      "\t},\n" +
                      "{\n" +
                      "\t\"startDate\":\"2023-10-27T15:00:00Z\",\n" +
                      "\t\"finishDate\":\"2023-10-27T17:00:00Z\",\n" +
                      "\t\"coordinates\":{\n" +
                      "\t\t\"latitude\":1,\n" +
                      "\t\t\"longitude\":1\n" +
                      "\t\t}\n" +
                      "\t}\n" +
                      "\t],\n" +
                      "\t\"tags\":[\"Social\"]\n" +
                      "}\t";
        MockMultipartFile image1 =
                new MockMultipartFile("images", "image1.jpg", "image/jpeg",
                        "image data".getBytes());
        MockMultipartFile image2 =
                new MockMultipartFile("images", "image2.png", "image/jpeg",
                        "image data 2".getBytes());

        when(userService.findByEmail(anyString())).thenReturn(userVO);
        when(eventService.save(any(AddEventDtoRequest.class), any(MultipartFile[].class), anyLong()))
                .thenReturn(new EventDto());

        MockMultipartFile jsonFile =
                new MockMultipartFile("addEventDtoRequest", "", "application/json", json.getBytes());
        mockMvc.perform(multipart(eventsLink + "/create")
                        .file(image1)
                        .file(image2)
                        .file(jsonFile)
                        .principal(principal)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isCreated());

        verify(eventService, times(1))
                .save(any(AddEventDtoRequest.class), any(MultipartFile[].class), anyLong());
    }

    @Test
    void delete() {
    }

    @Test
    void update() {
    }
}
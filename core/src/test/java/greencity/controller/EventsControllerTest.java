package greencity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import greencity.ModelUtils;
import greencity.converters.UserArgumentResolver;
import greencity.dto.event.AddEventDtoRequest;
import greencity.dto.event.EventAttenderDto;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static greencity.ModelUtils.getPrincipal;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    void createEventTest() throws Exception {
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
    void createEventBadRequestTest() throws Exception {
        mockMvc.perform(multipart(eventsLink + "/create")
                .content("{}")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateEvent() throws Exception {
        UserVO userVO = mock(UserVO.class);
        when(userVO.getId()).thenReturn(3L);

        String json = "{\n" +
                "\"id\": 1,\n" +
                "\"title\":\"test events\",\n" +
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

        when(userService.findByEmail(anyString())).thenReturn(userVO);
        when(eventService.update(any(AddEventDtoRequest.class), any(MultipartFile[].class), anyLong()))
                .thenReturn(new EventDto());

        MockMultipartFile jsonFile =
                new MockMultipartFile("addEventDtoRequest", "", "application/json", json.getBytes());

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(eventsLink + "/update");
        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });
        MockMultipartFile image3 =
                new MockMultipartFile("images", "image3.jpg", "image/jpeg",
                        "image data".getBytes());

        mockMvc.perform(builder
                        .file(jsonFile)
                        .file(image3)
                        .principal(principal)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk());

        verify(eventService, times(1))
                .update(any(AddEventDtoRequest.class), any(MultipartFile[].class), anyLong());
    }

    @Test
    void testGetAllEvents() throws Exception {
        int pageNumber = 1;
        int pageSize = 20;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        mockMvc.perform(get(eventsLink + "?page=1"))
                .andExpect(status().isOk());

        verify(eventService).findAll(pageable);
    }

    @Test
    void testGetEventsCreatedByUser() throws Exception {
        int pageNumber = 5;
        int pageSize = 2;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        mockMvc.perform(get(eventsLink + "/myEvents/createdEvents?page=5&size=2"))
                .andExpect(status().isOk());
        verify(eventService, times(1)).findAllByUser(null, pageable);
    }

    @Test
    void testGetAllEventSubscribers() throws Exception {
        Long eventId = 1L;

        List<EventAttenderDto> subscribers = new ArrayList<>();
        subscribers.add(new EventAttenderDto());
        subscribers.add(new EventAttenderDto());
        when(eventService.getAllSubscribers(eventId)).thenReturn(subscribers);

        mockMvc.perform(get(eventsLink + "/getAllSubscribers/{eventId}", eventId))
                .andExpect(status().isOk());
        verify(eventService, times(1)).getAllSubscribers(eventId);
    }

    @Test
    void testAddAttender() throws Exception {
        Long eventId = 1L;
        UserVO userVO = ModelUtils.getUserVO();
        when(userService.findByEmail(anyString())).thenReturn(userVO);
        mockMvc.perform(post(eventsLink + "/addAttender/{eventId}", eventId)
                        .principal(principal))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(eventService, times(1)).addAttenderToEvent(eventId, userVO.getId());
    }

    @Test
    void testRemoveAttender() throws Exception {
        Long eventId = 1L;
        UserVO userVO = ModelUtils.getUserVO();
        when(userService.findByEmail(anyString())).thenReturn(userVO);
        mockMvc.perform(MockMvcRequestBuilders.delete(eventsLink + "/removeAttender/{eventId}", eventId)
                        .principal(principal))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(eventService, times(1)).removeAttenderFromEvent(eq(eventId), anyLong());
    }
}
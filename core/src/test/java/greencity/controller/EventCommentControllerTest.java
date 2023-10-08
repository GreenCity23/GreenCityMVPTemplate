package greencity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import greencity.config.SecurityConfig;
import greencity.converters.UserArgumentResolver;
import greencity.dto.eventcomments.AddEventCommentDtoRequest;
import greencity.dto.user.UserVO;
import greencity.service.EventCommentService;
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
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;

import static greencity.ModelUtils.getPrincipal;
import static greencity.ModelUtils.getUserVO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration
@Import(SecurityConfig.class)
class EventCommentControllerTest {
    private static final String eventCommentControllerLink = "/events/comments";
    private MockMvc mockMvc;
    @InjectMocks
    private EventCommentController eventCommentController;
    @Mock
    private EventCommentService eventCommentService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private UserService userService;
    private Principal principal = getPrincipal();

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(eventCommentController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver(),
                        new UserArgumentResolver(userService, modelMapper))
                .build();
    }

    @Test
    void addComment() throws Exception {
        UserVO userVO = getUserVO();
        when(userService.findByEmail(anyString())).thenReturn(userVO);
        when(modelMapper.map(userVO, UserVO.class)).thenReturn(userVO);
        String content = "{\n"
                         + "  \"parentCommentId\": 0,\n"
                         + "  \"text\": \"string\"\n"
                         + "}";
        mockMvc.perform(post(eventCommentControllerLink + "/{eventId}", 1)
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isCreated());

        ObjectMapper mapper = new ObjectMapper();
        AddEventCommentDtoRequest addEventCommentDtoRequest = mapper.readValue(content, AddEventCommentDtoRequest.class);

        verify(userService).findByEmail("test@gmail.com");
        verify(eventCommentService).save(addEventCommentDtoRequest, 1L, userVO);
    }

    @Test
    void saveBadRequestTest() throws Exception {
        mockMvc.perform(post(eventCommentControllerLink + "/{eventId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCountOfComments() throws Exception {
        mockMvc.perform(get(eventCommentControllerLink + "/count/{eventId}", 1))
                .andExpect(status().isOk());

        verify(eventCommentService).getEventCommentsAmount(1L);
    }

    @Test
    void deleteTest() throws Exception {
        UserVO userVO = getUserVO();
        when(userService.findByEmail(anyString())).thenReturn(userVO);

        mockMvc.perform(delete(eventCommentControllerLink + "/{eventCommentId}", 1L)
                        .principal(principal))
                .andExpect(status().isOk());

        verify(userService).findByEmail("test@gmail.com");
        verify(eventCommentService).deleteById(1L, userVO);
    }

    @Test
    void getCountOfReplies() throws Exception {
        mockMvc.perform(get(eventCommentControllerLink + "/replies/count/{parentCommentId}", 1L))
                .andExpect(status().isOk());

        verify(eventCommentService).getCountOfCommentReplies(1L);
    }

    @Test
    void getAllActiveComments() throws Exception {
        UserVO userVO = getUserVO();
        when(userService.findByEmail(anyString())).thenReturn(userVO);

        int pageNumber = 5;
        int pageSize = 20;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        mockMvc.perform(get(eventCommentControllerLink + "/active?eventId=1&page=5")
                        .principal(principal))
                .andExpect(status().isOk());

        verify(userService).findByEmail("test@gmail.com");
        verify(eventCommentService).getAllActiveComments(pageable, userVO, 1L);
    }

    @Test
    void findAllActiveReplies() throws Exception {
        UserVO userVO = getUserVO();
        when(userService.findByEmail(anyString())).thenReturn(userVO);

        int pageNumber = 5;
        int pageSize = 20;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        mockMvc.perform(get(eventCommentControllerLink + "/replies/active/{parentCommentId}?page=5&size=20", 1)
                        .principal(principal))
                .andExpect(status().isOk());

        verify(userService).findByEmail("test@gmail.com");
        verify(eventCommentService).getAllActiveReplies(pageable, 1L, userVO);
    }


    @Test
    void updateComment() throws Exception {
        UserVO userVO = getUserVO();
        when(userService.findByEmail(anyString())).thenReturn(userVO);

        mockMvc.perform(patch(eventCommentControllerLink + "?id=1&commentText=great thing")
                        .principal(principal))
                .andExpect(status().isOk());

        verify(userService).findByEmail("test@gmail.com");
        verify(eventCommentService).update(1L, "great thing", userVO);
    }
}
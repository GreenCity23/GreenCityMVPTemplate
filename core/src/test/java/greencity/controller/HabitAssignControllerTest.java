package greencity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import greencity.config.SecurityConfig;
import greencity.converters.UserArgumentResolver;
import greencity.dto.habit.HabitAssignCustomPropertiesDto;
import greencity.dto.habit.HabitAssignStatDto;
import greencity.dto.habit.UpdateUserShoppingListDto;
import greencity.dto.habit.UserShoppingAndCustomShoppingListsDto;
import greencity.dto.user.UserVO;
import greencity.exception.handler.CustomExceptionHandler;
import greencity.service.HabitAssignService;
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
import org.springframework.context.annotation.Import;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

import static greencity.ModelUtils.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration
@Import(SecurityConfig.class)
class HabitAssignControllerTest {
    private final String habitAssignLink = "/habit/assign";
    private MockMvc mockMvc;

    @InjectMocks
    private HabitAssignController habitAssignController;
    @Mock
    private HabitAssignService habitAssignService;
    @Mock
    private UserService userService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private ObjectMapper objectMapper;

    private final Principal principal = getPrincipal();

    private final ErrorAttributes errorAttributes = new DefaultErrorAttributes();

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(habitAssignController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver(),
                new UserArgumentResolver(userService, modelMapper))
            .setControllerAdvice(new CustomExceptionHandler(errorAttributes, objectMapper))
            .build();
    }

    @Test
    void testAssignDefaultHabit() throws Exception {
        Long habitId = 1L;
        UserVO userVO = getUserVO();
        when(userService.findByEmail(anyString())).thenReturn(userVO);

        mockMvc.perform(post(habitAssignLink + "/{habitId}", habitId)
            .principal(principal))
            .andExpect(status().isCreated());

        verify(habitAssignService).assignDefaultHabitForUser(habitId, userVO);
    }

    @Test
    void testAssignCustomHabit() throws Exception {
        Long habitId = 1L;
        UserVO userVO = getUserVO();
        when(userService.findByEmail(anyString())).thenReturn(userVO);

        String content = "{\n" +
            "  \"friendsIdsList\": [\n" +
            "    1, 2\n" +
            "  ],\n" +
            "  \"habitAssignPropertiesDto\": {\n" +
            "    \"defaultShoppingListItems\": [\n" +
            "      1, 2\n" +
            "    ],\n" +
            "    \"duration\": 20\n" +
            "  }\n" +
            "}";

        mockMvc.perform(post(habitAssignLink + "/{habitId}/custom", habitId)
            .principal(principal)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isCreated());

        ObjectMapper mapper = new ObjectMapper();
        HabitAssignCustomPropertiesDto habitAssignCustomPropertiesDto = mapper.readValue(content,
            HabitAssignCustomPropertiesDto.class);

        verify(habitAssignService).assignCustomHabitForUser(habitId, userVO, habitAssignCustomPropertiesDto);
    }

    @Test
    void testGetHabitAssign() throws Exception {
        Long habitAssignId = 1L;
        UserVO userVO = getUserVO();
        when(userService.findByEmail(anyString())).thenReturn(userVO);

        mockMvc.perform(get(habitAssignLink + "/{habitAssignId}", habitAssignId)
            .principal(principal))
            .andExpect(status().isOk());

        verify(habitAssignService).getByHabitAssignIdAndUserId(habitAssignId, userVO.getId(), Locale.UK.getLanguage());
    }

    @Test
    void testUpdateAssignByHabitAssignId() throws Exception {
        Long habitAssignId = 1L;

        String content = "{\n" +
            "  \"status\": \"INPROGRESS\"\n" +
            "}";

        mockMvc.perform(patch(habitAssignLink + "/{habitAssignId}", habitAssignId)
            .principal(principal)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isOk());

        ObjectMapper mapper = new ObjectMapper();
        HabitAssignStatDto habitAssignStatDto = mapper.readValue(content, HabitAssignStatDto.class);

        verify(habitAssignService).updateStatusByHabitAssignId(habitAssignId, habitAssignStatDto);
    }

    @Test
    void testUpdateHabitAssignDuration() throws Exception {
        Long habitAssignId = 1L;
        UserVO userVO = getUserVO();
        int duration = 17;
        when(userService.findByEmail(anyString())).thenReturn(userVO);

        mockMvc.perform(put(habitAssignLink + "/{habitAssignId}/update-habit-duration", habitAssignId)
            .principal(principal)
            .param("duration", String.valueOf(duration)))
            .andExpect(status().isOk());

        verify(habitAssignService).updateUserHabitInfoDuration(habitAssignId, userVO.getId(), duration);
    }

    @Test
    void testEnrollHabit() throws Exception {
        Long habitAssignId = 1L;
        UserVO userVO = getUserVO();
        LocalDate date = LocalDate.from(getHabitAssign().getCreateDate());
        when(userService.findByEmail(anyString())).thenReturn(userVO);

        mockMvc.perform(post(habitAssignLink + "/{habitAssignId}/enroll/{date}", habitAssignId, date)
            .principal(principal))
            .andExpect(status().isOk());

        verify(habitAssignService).enrollHabit(habitAssignId, userVO.getId(), date, Locale.UK.getLanguage());
    }

    @Test
    void testUnEnrollHabit() throws Exception {
        Long habitAssignId = 1L;
        UserVO userVO = getUserVO();
        LocalDate date = LocalDate.from(getHabitAssign().getCreateDate());
        when(userService.findByEmail(anyString())).thenReturn(userVO);

        mockMvc.perform(post(habitAssignLink + "/{habitAssignId}/unenroll/{date}", habitAssignId, date)
            .principal(principal))
            .andExpect(status().isOk());

        verify(habitAssignService).unenrollHabit(habitAssignId, userVO.getId(), date);
    }

    @Test
    void testGetHabitAssignBetweenDates() throws Exception {
        UserVO userVO = getUserVO();
        LocalDate fromDate = LocalDate.now().minusDays(1);
        LocalDate toDate = LocalDate.now();
        when(userService.findByEmail(anyString())).thenReturn(userVO);

        mockMvc.perform(get(habitAssignLink + "/activity/{from}/to/{to}", fromDate, toDate)
            .principal(principal))
            .andExpect(status().isOk());

        verify(habitAssignService).findHabitAssignsBetweenDates(userVO.getId(), fromDate, toDate,
            Locale.UK.getLanguage());
    }

    @Test
    void testCancelHabitAssign() throws Exception {
        Long habitId = 1L;
        UserVO userVO = getUserVO();
        when(userService.findByEmail(anyString())).thenReturn(userVO);

        mockMvc.perform(patch(habitAssignLink + "/cancel/{habitId}", habitId)
            .principal(principal))
            .andExpect(status().isOk());

        verify(habitAssignService).cancelHabitAssign(habitId, userVO.getId());
    }

    @Test
    void testGetHabitAssignByHabitId() throws Exception {
        Long habitId = 1L;
        UserVO userVO = getUserVO();
        when(userService.findByEmail(anyString())).thenReturn(userVO);

        mockMvc.perform(get(habitAssignLink + "/{habitId}/active", habitId)
            .principal(principal))
            .andExpect(status().isOk());

        verify(habitAssignService).findHabitAssignByUserIdAndHabitId(userVO.getId(), habitId, Locale.UK.getLanguage());
    }

    @Test
    void testGetCurrentUserHabitAssignsByIdAndAcquired() throws Exception {
        UserVO userVO = getUserVO();
        when(userService.findByEmail(anyString())).thenReturn(userVO);

        mockMvc.perform(get(habitAssignLink + "/allForCurrentUser")
            .principal(principal))
            .andExpect(status().isOk());

        verify(habitAssignService).getAllHabitAssignsByUserIdAndStatusNotCancelled(userVO.getId(),
            Locale.UK.getLanguage());
    }

    @Test
    void testDeleteHabitAssign() throws Exception {
        Long habitAssignId = 1L;
        UserVO userVO = getUserVO();
        when(userService.findByEmail(anyString())).thenReturn(userVO);

        mockMvc.perform(delete(habitAssignLink + "/delete/{habitAssignId}", habitAssignId)
            .principal(principal))
            .andExpect(status().isOk());

        verify(habitAssignService).deleteHabitAssign(habitAssignId, userVO.getId());
    }

    @Test
    void testUpdateShoppingListStatus() throws Exception {
        String content = "{\n" +
            "  \"habitAssignId\": 1,\n" +
            "  \"userShoppingListAdvanceDto\": [\n" +
            "    {\n" +
            "      \"content\": \"string\",\n" +
            "      \"dateCompleted\": \"" + LocalDateTime.now() + "\",\n" +
            "      \"id\": 0,\n" +
            "      \"shoppingListItemId\": 0,\n" +
            "      \"status\": \"ACTIVE\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"userShoppingListItemId\": 0\n" +
            "}";

        mockMvc.perform(put(habitAssignLink + "/saveShoppingListForHabitAssign")
            .principal(principal)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isOk());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        UpdateUserShoppingListDto updateUserShoppingListDto = mapper.readValue(content,
            UpdateUserShoppingListDto.class);

        verify(habitAssignService).updateUserShoppingListItem(updateUserShoppingListDto);
    }

    @Test
    void testGetAllHabitAssignsByHabitIdAndAcquired() throws Exception {
        Long habitId = 1L;

        mockMvc.perform(get(habitAssignLink + "/{habitId}/all", habitId)
            .principal(principal))
            .andExpect(status().isOk());

        verify(habitAssignService).getAllHabitAssignsByHabitIdAndStatusNotCancelled(habitId, Locale.UK.getLanguage());
    }

    @Test
    void testGetInProgressHabitAssignOnDate() throws Exception {
        UserVO userVO = getUserVO();
        LocalDate date = LocalDate.now();
        when(userService.findByEmail(anyString())).thenReturn(userVO);

        mockMvc.perform(get(habitAssignLink + "/active/{date}", date)
            .principal(principal))
            .andExpect(status().isOk());

        verify(habitAssignService).findInprogressHabitAssignsOnDate(userVO.getId(), date, Locale.UK.getLanguage());

    }

    @Test
    void testGetUsersHabitByHabitId() throws Exception {
        Long habitAssignId = 1L;
        UserVO userVO = getUserVO();
        when(userService.findByEmail(anyString())).thenReturn(userVO);

        mockMvc.perform(get(habitAssignLink + "/{habitAssignId}/more", habitAssignId)
            .principal(principal))
            .andExpect(status().isOk());

        verify(habitAssignService).findHabitByUserIdAndHabitAssignId(userVO.getId(), habitAssignId,
            Locale.UK.getLanguage());
    }

    @Test
    void testGetUserAndCustomListByUserIdAndHabitId() throws Exception {
        Long habitAssignId = 1L;
        UserVO userVO = getUserVO();
        when(userService.findByEmail(anyString())).thenReturn(userVO);

        mockMvc.perform(get(habitAssignLink + "/{habitAssignId}/allUserAndCustomList", habitAssignId)
            .principal(principal))
            .andExpect(status().isOk());

        verify(habitAssignService).getUserShoppingAndCustomShoppingLists(userVO.getId(), habitAssignId,
            Locale.UK.getLanguage());
    }

    @Test
    void testGetListOfUserAndCustomShoppingListsInProgress() throws Exception {
        UserVO userVO = getUserVO();
        when(userService.findByEmail(anyString())).thenReturn(userVO);

        mockMvc.perform(get(habitAssignLink + "/allUserAndCustomShoppingListsInprogress")
            .principal(principal))
            .andExpect(status().isOk());

        verify(habitAssignService).getListOfUserAndCustomShoppingListsWithStatusInprogress(userVO.getId(),
            Locale.UK.getLanguage());
    }

    @Test
    void testUpdateUserAndCustomShoppingLists() throws Exception {
        Long habitAssignId = 1L;
        UserVO userVO = getUserVO();
        when(userService.findByEmail(anyString())).thenReturn(userVO);

        String content = "{\n" +
            "  \"customShoppingListItemDto\": [\n" +
            "    {\n" +
            "      \"id\": 1,\n" +
            "      \"status\": \"ACTIVE\",\n" +
            "      \"text\": \"string\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"userShoppingListItemDto\": [\n" +
            "    {\n" +
            "      \"id\": 1,\n" +
            "      \"status\": \"ACTIVE\",\n" +
            "      \"text\": \"string\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

        mockMvc.perform(put(habitAssignLink + "/{habitAssignId}/allUserAndCustomList", habitAssignId)
            .principal(principal)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isOk());

        ObjectMapper mapper = new ObjectMapper();
        UserShoppingAndCustomShoppingListsDto listsDto = mapper.readValue(content,
            UserShoppingAndCustomShoppingListsDto.class);

        verify(habitAssignService).fullUpdateUserAndCustomShoppingLists(userVO.getId(), habitAssignId, listsDto,
            Locale.UK.getLanguage());
    }

    @Test
    void testUpdateProgressNotificationHasDisplayed() throws Exception {
        Long habitAssignId = 1L;
        UserVO userVO = getUserVO();
        when(userService.findByEmail(anyString())).thenReturn(userVO);

        mockMvc.perform(put(habitAssignLink + "/{habitAssignId}/updateProgressNotificationHasDisplayed",
            habitAssignId)
                .principal(principal))
            .andExpect(status().isOk());

        verify(habitAssignService).updateProgressNotificationHasDisplayed(habitAssignId, userVO.getId());
    }
}

package greencity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import greencity.config.SecurityConfig;
import greencity.converters.UserArgumentResolver;
import greencity.dto.habitstatistic.AddHabitStatisticDto;
import greencity.dto.habitstatistic.UpdateHabitStatisticDto;
import greencity.dto.user.UserVO;
import greencity.exception.exceptions.NotFoundException;
import greencity.exception.handler.CustomExceptionHandler;
import greencity.service.HabitStatisticService;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;

import static greencity.ModelUtils.getPrincipal;
import static greencity.ModelUtils.getUserVO;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration
@Import(SecurityConfig.class)
class HabitStatisticControllerTest {
    private static final String habitStatisticControllerLink = "/habit/statistic";
    private MockMvc mockMvc;
    @InjectMocks
    private HabitStatisticController habitStatisticController;
    @Mock
    private HabitStatisticService habitStatisticService;
    @Mock
    private UserService userService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private ObjectMapper objectMapper;
    private final Principal principal = getPrincipal();
    private final ErrorAttributes errorAttributes = new DefaultErrorAttributes();

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(habitStatisticController)
            .setCustomArgumentResolvers(new UserArgumentResolver(userService, modelMapper))
            .setControllerAdvice(new CustomExceptionHandler(errorAttributes, objectMapper))
            .build();
    }

    @Test
    void saveHabitStatisticTest() throws Exception {
        UserVO userVO = getUserVO();
        when(userService.findByEmail(anyString())).thenReturn(userVO);
        when(modelMapper.map(userVO, UserVO.class)).thenReturn(userVO);

        String content = "{\n" +
            "  \"amountOfItems\": 1,\n" +
            "  \"createDate\": \"2023-09-12T17:14:28.616Z\",\n" +
            "  \"habitRate\": \"DEFAULT\"\n" +
            "}";

        mockMvc.perform(post(habitStatisticControllerLink + "/{habitId}", 1)
            .principal(principal)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isCreated());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        AddHabitStatisticDto addHabitStatisticDto = mapper.readValue(content, AddHabitStatisticDto.class);

        verify(habitStatisticService)
            .saveByHabitIdAndUserId(1L, userVO.getId(), addHabitStatisticDto);
    }

    @Test
    void saveHabitStatisticHabitNotFoundTest() throws Exception {
        UserVO userVO = getUserVO();
        when(userService.findByEmail(anyString())).thenReturn(userVO);
        when(modelMapper.map(userVO, UserVO.class)).thenReturn(userVO);

        String content = "{\n" +
            "  \"amountOfItems\": 2,\n" +
            "  \"createDate\": \"2023-10-11T17:14:28.616Z\",\n" +
            "  \"habitRate\": \"DEFAULT\"\n" +
            "}";
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        AddHabitStatisticDto addHabitStatisticDto = mapper.readValue(content, AddHabitStatisticDto.class);

        when(habitStatisticService.saveByHabitIdAndUserId(2L, userVO.getId(), addHabitStatisticDto))
            .thenThrow(NotFoundException.class);

        mockMvc.perform(post(habitStatisticControllerLink + "/{habitId}", 2)
            .principal(principal)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isNotFound());

        verify(habitStatisticService)
            .saveByHabitIdAndUserId(2L, userVO.getId(), addHabitStatisticDto);
    }

    @Test
    void saveHabitStatisticBadRequestTest() throws Exception {
        mockMvc.perform(post(habitStatisticControllerLink + "/{habitId}", 1)
            .principal(principal)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateStatisticTest() throws Exception {
        UserVO userVO = getUserVO();
        when(userService.findByEmail(anyString())).thenReturn(userVO);
        when(modelMapper.map(userVO, UserVO.class)).thenReturn(userVO);

        String content = "{\n" +
            "  \"amountOfItems\": 0,\n" +
            "  \"habitRate\": \"DEFAULT\"\n" +
            "}";

        mockMvc.perform(put(habitStatisticControllerLink + "/{id}", 1)
            .principal(principal)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isOk());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        UpdateHabitStatisticDto updateHabitStatisticDto = mapper.readValue(content, UpdateHabitStatisticDto.class);

        verify(habitStatisticService)
            .update(1L, userVO.getId(), updateHabitStatisticDto);
    }

    @Test
    void updateHabitStatisticHabitNotFoundTest() throws Exception {
        UserVO userVO = getUserVO();
        when(userService.findByEmail(anyString())).thenReturn(userVO);
        when(modelMapper.map(userVO, UserVO.class)).thenReturn(userVO);

        String content = "{\n" +
            "  \"amountOfItems\": 0,\n" +
            "  \"habitRate\": \"DEFAULT\"\n" +
            "}";

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        UpdateHabitStatisticDto updateHabitStatisticDto = mapper.readValue(content, UpdateHabitStatisticDto.class);

        when(habitStatisticService.update(2L, userVO.getId(), updateHabitStatisticDto))
            .thenThrow(NotFoundException.class);

        mockMvc.perform(put(habitStatisticControllerLink + "/{id}", 2)
            .principal(principal)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isNotFound());

        verify(habitStatisticService)
            .update(2L, userVO.getId(), updateHabitStatisticDto);
    }

    @Test
    void updateStatisticsBadRequestTest() throws Exception {
        mockMvc.perform(put(habitStatisticControllerLink + "/{id}", 1)
            .principal(principal)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void findAllByHabitIdTest() throws Exception {
        mockMvc.perform(get(habitStatisticControllerLink + "/{habitId}", 1))
            .andExpect(status().isOk());

        verify(habitStatisticService).findAllStatsByHabitId(1L);
    }

    @Test
    void findAllStatsByHabitAssignIdTest() throws Exception {
        mockMvc.perform(get(habitStatisticControllerLink + "/assign/{habitAssignId}", 1))
            .andExpect(status().isOk());

        verify(habitStatisticService).findAllStatsByHabitAssignId(1L);
    }

    @Test
    void getTodayStatisticsForAllHabitItemsTest() throws Exception {
        mockMvc.perform(get(habitStatisticControllerLink + "/todayStatisticsForAllHabitItems")
            .param("locale", "en"))
            .andExpect(status().isOk());

        verify(habitStatisticService).getTodayStatisticsForAllHabitItems("en");
    }

    @Test
    void findAmountOfAcquiredHabitsTest() throws Exception {
        mockMvc.perform(get(habitStatisticControllerLink + "/acquired/count")
            .param("userId", String.valueOf(1)))
            .andExpect(status().isOk());

        verify(habitStatisticService).getAmountOfAcquiredHabitsByUserId(1L);
    }

    @Test
    void findAmountOfHabitsInProgressTest() throws Exception {
        mockMvc.perform(get(habitStatisticControllerLink + "/in-progress/count")
            .param("userId", String.valueOf(1)))
            .andExpect(status().isOk());

        verify(habitStatisticService).getAmountOfHabitsInProgressByUserId(1L);
    }
}
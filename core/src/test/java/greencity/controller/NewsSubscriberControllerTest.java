package greencity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import greencity.dto.newssubscriber.NewsSubscriberRequestDto;
import greencity.dto.newssubscriber.NewsSubscriberResponseDto;
import greencity.service.NewsSubscriberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Arthur Mkrtchian
 */

@ExtendWith(MockitoExtension.class)
class NewsSubscriberControllerTest {

    @Mock
    private NewsSubscriberService newsSubscriberService;

    @InjectMocks
    private NewsSubscriberController newsSubscriberController;

    private MockMvc mockMvc;

    private final String clientAddress = "http://greencity.ua";
    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(newsSubscriberController)
                .build();
        ReflectionTestUtils.setField(newsSubscriberController, "clientAddress", clientAddress);
    }

    @Test
    public void testGetAllSubscribers() throws Exception {
        List<NewsSubscriberResponseDto> mockList = List.of(
                new NewsSubscriberResponseDto(1L, "example1@mail.com", "UUID_UNSUBSCRIBE_TOKEN", "UUID_CONFIRMATION_TOKEN"),
                new NewsSubscriberResponseDto(2L, "example2@mail.com", "UUID_UNSUBSCRIBE_TOKEN", "UUID_CONFIRMATION_TOKEN")
        );

        when(newsSubscriberService.getAllSubscribers()).thenReturn(mockList);

        mockMvc.perform(get("/newsSubscriber")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].email").value(mockList.get(0).getEmail()))
                .andExpect(jsonPath("$[1].email").value(mockList.get(1).getEmail()));

        verify(newsSubscriberService, times(1)).getAllSubscribers();
        verifyNoMoreInteractions(newsSubscriberService);
    }

    @Test
    public void testSaveSubscriber() throws Exception {
        NewsSubscriberRequestDto dto = new NewsSubscriberRequestDto("example@mail.com");

        when(newsSubscriberService.saveSubscriber(any(NewsSubscriberRequestDto.class))).thenReturn(
                new NewsSubscriberResponseDto(2L, "example2@mail.com", "UUID_UNSUBSCRIBE_TOKEN", "UUID_SUBSCRIBE_TOKEN")
        );

        mockMvc.perform(post("/newsSubscriber")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept", "application/json")
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.email").value(dto.getEmail()));

        verify(newsSubscriberService, times(1)).saveSubscriber(any(NewsSubscriberRequestDto.class));
        verifyNoMoreInteractions(newsSubscriberService);
    }

    @Test
    public void testUnsubscribe() throws Exception {
        String email = "example@mail.com";
        String unsubscribeToken = "UUID_UNSUBSCRIBE_TOKEN";

        when(newsSubscriberService.unsubscribe(email, unsubscribeToken)).thenReturn(true);

        mockMvc.perform(get("/newsSubscriber/unsubscribe")
                        .param("email", email)
                        .param("unsubscribeToken", unsubscribeToken))
                .andExpect(status().isSeeOther())
                .andExpect(header().string("Location", clientAddress));

        verify(newsSubscriberService, times(1)).unsubscribe(email, unsubscribeToken);
    }

    @Test
    public void testConfirmSubscription() throws Exception {
        String email = "example@mail.com";
        String confirmationToken = "UUID_CONFIRMATION_TOKEN";

        when(newsSubscriberService.confirmSubscription(email, confirmationToken)).thenReturn(true);

        mockMvc.perform(get("/newsSubscriber/confirm")
                        .param("email", email)
                        .param("confirmationToken", confirmationToken))
                .andExpect(status().isSeeOther())
                .andExpect(header().string("Location", clientAddress));

        verify(newsSubscriberService, times(1)).confirmSubscription(email, confirmationToken);
    }
}

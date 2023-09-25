package greencity.controller;

import greencity.service.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SearchControllerTest {
    private static final String searchLink = "/search";
    private static final String searchEcoNewsLink = "/search/econews";
    private MockMvc mockMvc;

    @Mock
    private SearchService searchService;

    @InjectMocks
    private SearchController searchController;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(searchController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
    }

    @Test
    void searchTest() throws Exception {
        mockMvc.perform(get(searchLink)
            .param("searchQuery", "test"))
            .andExpect(status().isOk());

        verify(searchService).search(anyString(), anyString());
    }

    @Test
    void searchEcoNewsTest() throws Exception {
        mockMvc.perform(get(searchEcoNewsLink)
            .param("searchQuery", "test"))
            .andExpect(status().isOk());

        verify(searchService).searchAllNews(any(), anyString(), any());
    }
}
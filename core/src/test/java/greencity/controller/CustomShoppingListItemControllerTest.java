package greencity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import greencity.controller.CustomShoppingListItemController;
import greencity.dto.shoppinglistitem.BulkSaveCustomShoppingListItemDto;
import greencity.dto.shoppinglistitem.CustomShoppingListItemResponseDto;
import greencity.service.CustomShoppingListItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CustomShoppingListItemControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private CustomShoppingListItemController customShoppingListItemController;

    @Mock
    private CustomShoppingListItemService customShoppingListItemService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customShoppingListItemController).build();
    }

    @Test
    void testGetAllAvailableCustomShoppingListItems() throws Exception {
        Long userId = 1L;
        Long habitId = 2L;

        List<CustomShoppingListItemResponseDto> itemList = new ArrayList<>();

        when(customShoppingListItemService.findAllAvailableCustomShoppingListItems(userId, habitId))
            .thenReturn(itemList);

        mockMvc.perform(get("/custom/shopping-list-items/{userId}/{habitId}", userId, habitId)
            .header("Accept", "application/json"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        verify(customShoppingListItemService, times(1)).findAllAvailableCustomShoppingListItems(userId, habitId);
        verifyNoMoreInteractions(customShoppingListItemService);
    }

    @Test
    void testSaveUserCustomShoppingListItems() throws Exception {
        Long userId = 1L;
        Long habitAssignId = 2L;
        BulkSaveCustomShoppingListItemDto dto = new BulkSaveCustomShoppingListItemDto();

        List<CustomShoppingListItemResponseDto> savedItems = new ArrayList<>();

        when(customShoppingListItemService.save(dto, userId, habitAssignId)).thenReturn(savedItems);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(dto);

        mockMvc
            .perform(post("/custom/shopping-list-items/{userId}/{habitAssignId}/custom-shopping-list-items", userId,
                habitAssignId)
                    .header("Accept", "application/json")
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .content(requestJson))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        verify(customShoppingListItemService, times(1)).save(dto, userId, habitAssignId);
        verifyNoMoreInteractions(customShoppingListItemService);
    }

    @Test
    void testUpdateItemStatus() throws Exception {
        Long userId = 1L;
        Long itemId = 3L;
        String itemStatus = "DONE";

        CustomShoppingListItemResponseDto updatedItem = new CustomShoppingListItemResponseDto();

        when(customShoppingListItemService.updateItemStatus(userId, itemId, itemStatus)).thenReturn(updatedItem);

        mockMvc.perform(patch("/custom/shopping-list-items/{userId}/custom-shopping-list-items", userId)
            .header("Accept", "application/json")
            .param("itemId", itemId.toString())
            .param("status", itemStatus))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        verify(customShoppingListItemService, times(1)).updateItemStatus(userId, itemId, itemStatus);
        verifyNoMoreInteractions(customShoppingListItemService);
    }

    @Test
    void testUpdateItemStatusToDone() throws Exception {
        Long userId = 1L;
        Long itemId = 3L;

        mockMvc.perform(patch("/custom/shopping-list-items/{userId}/done?itemId={itemId}", userId, itemId))
            .andExpect(status().isOk());

        verify(customShoppingListItemService, times(1)).updateItemStatusToDone(userId, itemId);
        verifyNoMoreInteractions(customShoppingListItemService);
    }

    @Test
    void testBulkDeleteCustomShoppingListItems() throws Exception {
        Long userId = 1L;
        String ids = "1,2,3";

        List<Long> deletedItemIds = new ArrayList<>();

        when(customShoppingListItemService.bulkDelete(ids)).thenReturn(deletedItemIds);

        mockMvc
            .perform(delete("/custom/shopping-list-items/{userId}/custom-shopping-list-items?ids={ids}", userId, ids))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        verify(customShoppingListItemService, times(1)).bulkDelete(ids);
        verifyNoMoreInteractions(customShoppingListItemService);
    }

    @Test
    void testGetAllCustomShoppingItemsByStatus() throws Exception {
        Long userId = 1L;
        String status = "ACTIVE";

        List<CustomShoppingListItemResponseDto> itemList = new ArrayList<>();

        when(customShoppingListItemService.findAllUsersCustomShoppingListItemsByStatus(userId, status))
            .thenReturn(itemList);

        mockMvc
            .perform(
                get("/custom/shopping-list-items/{userId}/custom-shopping-list-items?status={status}", userId, status))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

        verify(customShoppingListItemService, times(1)).findAllUsersCustomShoppingListItemsByStatus(userId, status);
        verifyNoMoreInteractions(customShoppingListItemService);
    }
}

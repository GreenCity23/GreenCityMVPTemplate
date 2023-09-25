package greencity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import greencity.converters.UserArgumentResolver;
import greencity.dto.shoppinglistitem.ShoppingListItemDto;
import greencity.enums.ShoppingListItemStatus;
import greencity.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;

import java.util.*;

import static greencity.ModelUtils.getPrincipal;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import greencity.dto.shoppinglistitem.ShoppingListItemRequestDto;
import greencity.dto.user.UserShoppingListItemResponseDto;
import greencity.dto.user.UserVO;
import greencity.service.ShoppingListItemService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ShoppingListItemControllerTest {
    @InjectMocks
    private ShoppingListItemController shoppingListItemController;
    @Mock
    private ShoppingListItemService shoppingListItemService;
    @Mock
    private UserVO userVO;
    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserService userService;
    private Locale locale;
    private MockMvc mockMvc;
    private static final String shoppingListItemControllerLink = "/user/shopping-list-items";
    private final Principal principal = getPrincipal();

    @BeforeEach
    void setup() {
        when(userService.findByEmail(anyString())).thenReturn(userVO);
        when(modelMapper.map(userVO, UserVO.class)).thenReturn(userVO);

        this.mockMvc = MockMvcBuilders.standaloneSetup(shoppingListItemController)
            .setCustomArgumentResolvers(new UserArgumentResolver(userService, modelMapper))
            .build();
    }

    @Test
    void saveUserShoppingListItemsTest() throws Exception {
        long habitId = 1L;
        List<ShoppingListItemRequestDto> requestDtoList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(requestDtoList);

        List<UserShoppingListItemResponseDto> responseDtoList = new ArrayList<>();

        when(shoppingListItemService.saveUserShoppingListItems(anyLong(), anyLong(), anyList(), anyString()))
            .thenReturn(responseDtoList);

        mockMvc.perform(post(shoppingListItemControllerLink)
            .contentType("application/json")
            .content(jsonString)
            .param("habitId", Long.toString(habitId))
            .principal(principal)
            .param("locale", Locale.US.toLanguageTag()))
            .andExpect(status().isCreated());
    }

    @Test
    void getShoppingListItemsAssignedToUserTest() throws Exception {
        Long habitId = 1L;
        List<UserShoppingListItemResponseDto> shoppingListItems = Collections.singletonList(
            new UserShoppingListItemResponseDto(1L, "Item 1", ShoppingListItemStatus.ACTIVE));

        when(shoppingListItemService.getUserShoppingList(anyLong(), eq(habitId), anyString()))
            .thenReturn(shoppingListItems);

        mockMvc.perform(get(shoppingListItemControllerLink + "/habits/{habitId}/shopping-list", habitId)
            .principal(principal)
            .param("locale", Locale.US.toLanguageTag()))
            .andExpect(status().isOk());
        verify(shoppingListItemService).getUserShoppingList(anyLong(), eq(habitId), anyString());
    }

    @Test
    void deleteShoppingListItemTest() throws Exception {
        Long habitId = 1L;
        Long shoppingListItemId = 2L;

        mockMvc.perform(delete(shoppingListItemControllerLink)
            .param("habitId", habitId.toString())
            .param("shoppingListItemId", shoppingListItemId.toString())
            .principal(principal))
            .andExpect(status().isOk());

        verify(shoppingListItemService).deleteUserShoppingListItemByItemIdAndUserIdAndHabitId(
            shoppingListItemId, userVO.getId(), habitId);
    }

    @Test
    void updateUserShoppingListItemStatusTest() throws Exception {
        Long userShoppingListItemId = 1L;
        locale = Locale.US;

        mockMvc.perform(patch(shoppingListItemControllerLink + "/{userShoppingListItemId}", userShoppingListItemId)
            .principal(principal))
            .andExpect(status().isCreated());

        verify(shoppingListItemService).updateUserShopingListItemStatus(userVO.getId(), userShoppingListItemId,
            locale.getLanguage());
    }

    @Test
    void updateUserShoppingListItemStatusWithStatusTest() throws Exception {
        Long userShoppingListItemId = 1L;
        String status = "DONE";
        locale = Locale.US;

        mockMvc.perform(patch(shoppingListItemControllerLink + "/{userShoppingListItemId}/status/{status}",
            userShoppingListItemId, status)
                .principal(principal))
            .andExpect(status().isOk());

        verify(shoppingListItemService).updateUserShoppingListItemStatus(userVO.getId(), userShoppingListItemId,
            locale.getLanguage(), status);
    }

    @Test
    void findInProgressByUserIdTest() throws Exception {
        Long userId = 1L;
        String languageCode = "en";
        List<ShoppingListItemDto> inProgressItems = Collections.singletonList(new ShoppingListItemDto());

        when(shoppingListItemService.findInProgressByUserIdAndLanguageCode(userId, languageCode))
            .thenReturn(inProgressItems);

        mockMvc.perform(get(shoppingListItemControllerLink + "/{userId}/get-all-inprogress", userId)
            .param("lang", languageCode)
            .principal(principal))
            .andExpect(status().isOk());

        verify(shoppingListItemService).findInProgressByUserIdAndLanguageCode(userId, languageCode);
    }

    @Test
    void bulkDeleteUserShoppingListItemsTest1() throws Exception {
        String commaSeparatedIds = "1,2,3";
        List<Long> deletedIds = Arrays.asList(1L, 2L, 3L);

        when(shoppingListItemService.deleteUserShoppingListItems(commaSeparatedIds)).thenReturn(deletedIds);

        mockMvc.perform(delete(shoppingListItemControllerLink + "/user-shopping-list-items")
            .param("ids", commaSeparatedIds)
            .principal(principal))
            .andExpect(status().isOk());

        verify(shoppingListItemService).deleteUserShoppingListItems(commaSeparatedIds);
    }
}

package greencity.mapping;

import greencity.dto.shoppinglistitem.CustomShoppingListItemResponseDto;
import greencity.entity.CustomShoppingListItem;
import greencity.enums.ShoppingListItemStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomShoppingListResponseDtoMapperTest {

    CustomShoppingListResponseDtoMapper customShoppingListResponseDtoMapper;

    @BeforeEach
    void setUp() {
        customShoppingListResponseDtoMapper = new CustomShoppingListResponseDtoMapper();
    }

    @Test
    @DisplayName("CustomShoppingListItem convert to CustomShoppingListItemResponseDto test")
    void testConvert() {
        CustomShoppingListItem customShoppingListItem =
            new CustomShoppingListItem().setId(1L).setText("Test").setStatus(ShoppingListItemStatus.ACTIVE);

        CustomShoppingListItemResponseDto customShoppingListItemResponseDto =
            customShoppingListResponseDtoMapper.convert(customShoppingListItem);

        assertEquals(1L, customShoppingListItemResponseDto.getId());
        assertEquals("Test", customShoppingListItemResponseDto.getText());
        assertEquals(ShoppingListItemStatus.ACTIVE, customShoppingListItemResponseDto.getStatus());
    }

    @Test
    @DisplayName("CustomShoppingListItem list convert to CustomShoppingListItemResponseDto list test")
    void testMapAllToList() {
        List<CustomShoppingListItem> customShoppingListItems = Arrays.asList(
            new CustomShoppingListItem().setId(1L).setText("Test").setStatus(ShoppingListItemStatus.ACTIVE),
            new CustomShoppingListItem().setId(2L).setText("Test").setStatus(ShoppingListItemStatus.INPROGRESS),
            new CustomShoppingListItem().setId(3L).setText("Test").setStatus(ShoppingListItemStatus.DONE));

        List<CustomShoppingListItemResponseDto> actualList =
            customShoppingListResponseDtoMapper.mapAllToList(customShoppingListItems);

        List<CustomShoppingListItemResponseDto> expectedList = Arrays.asList(
            new CustomShoppingListItemResponseDto(1L, "Test", ShoppingListItemStatus.ACTIVE),
            new CustomShoppingListItemResponseDto(2L, "Test", ShoppingListItemStatus.INPROGRESS),
            new CustomShoppingListItemResponseDto(3L, "Test", ShoppingListItemStatus.DONE));

        assertThat(actualList).isEqualTo(expectedList);
    }
}
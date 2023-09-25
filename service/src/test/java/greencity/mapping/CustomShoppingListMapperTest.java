package greencity.mapping;

import greencity.dto.shoppinglistitem.CustomShoppingListItemResponseDto;
import greencity.entity.CustomShoppingListItem;
import greencity.enums.ShoppingListItemStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static greencity.ModelUtils.getCustomShoppingListItemResponseDto;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomShoppingListMapperTest {

    CustomShoppingListMapper customShoppingListMapper;

    @BeforeEach
    void setUp() {
        customShoppingListMapper = new CustomShoppingListMapper();
    }

    @Test
    @DisplayName("Map CustomShoppingListItemResponseDto to CustomShoppingListItem test")
    public void testConvert() {
        CustomShoppingListItemResponseDto customShoppingListItemResponseDto = getCustomShoppingListItemResponseDto();

        CustomShoppingListItem customShoppingListItem =
            customShoppingListMapper.convert(customShoppingListItemResponseDto);

        assertEquals(1L, customShoppingListItem.getId());
        assertEquals("TEXT", customShoppingListItem.getText());
        assertEquals(ShoppingListItemStatus.INPROGRESS, customShoppingListItem.getStatus());
    }

    @Test
    @DisplayName("Map CustomShoppingListItemResponseDto list to CustomShoppingListItem list test")
    public void mappAllToListTest() {
        List<CustomShoppingListItemResponseDto> customShoppingListItemResponseDtoList = Arrays.asList(
            new CustomShoppingListItemResponseDto().setId(1L).setText("Text")
                .setStatus(ShoppingListItemStatus.INPROGRESS),
            new CustomShoppingListItemResponseDto().setId(2L).setText("Text").setStatus(ShoppingListItemStatus.ACTIVE),
            new CustomShoppingListItemResponseDto().setId(3L).setText("Text").setStatus(ShoppingListItemStatus.DONE));

        List<CustomShoppingListItem> actualList =
            customShoppingListMapper.mapAllToList(customShoppingListItemResponseDtoList);

        List<CustomShoppingListItem> expectedList = Arrays.asList(
            new CustomShoppingListItem().setId(1L).setText("Text").setStatus(ShoppingListItemStatus.INPROGRESS),
            new CustomShoppingListItem().setId(2L).setText("Text").setStatus(ShoppingListItemStatus.ACTIVE),
            new CustomShoppingListItem().setId(3L).setText("Text").setStatus(ShoppingListItemStatus.DONE));
        assertThat(actualList).isEqualTo(expectedList);
    }
}

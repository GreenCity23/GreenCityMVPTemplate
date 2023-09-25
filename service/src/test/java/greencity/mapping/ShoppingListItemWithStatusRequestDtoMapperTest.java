package greencity.mapping;

import greencity.dto.shoppinglistitem.ShoppingListItemWithStatusRequestDto;
import greencity.entity.ShoppingListItem;
import greencity.entity.UserShoppingListItem;
import greencity.enums.ShoppingListItemStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Arthur Mkrtchian
 */
class ShoppingListItemWithStatusRequestDtoMapperTest {

    ShoppingListItemWithStatusRequestDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ShoppingListItemWithStatusRequestDtoMapper();
    }

    @Test
    @DisplayName("Convert ShoppingListItemWithStatusRequestDto to UserShoppingListItem test")
    void convertTest() {
        ShoppingListItemWithStatusRequestDto shoppingListItemWithStatusRequestDto =
            new ShoppingListItemWithStatusRequestDto();
        shoppingListItemWithStatusRequestDto.setStatus(ShoppingListItemStatus.INPROGRESS);

        UserShoppingListItem expected = UserShoppingListItem.builder()
            .status(ShoppingListItemStatus.INPROGRESS)
            .shoppingListItem(ShoppingListItem.builder().build())
            .build();

        UserShoppingListItem actual = mapper.convert(shoppingListItemWithStatusRequestDto);

        assertEquals(expected, actual);
    }
}

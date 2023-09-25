package greencity.mapping;

import greencity.dto.shoppinglistitem.ShoppingListItemRequestDto;
import greencity.entity.ShoppingListItem;
import greencity.entity.UserShoppingListItem;
import greencity.enums.ShoppingListItemStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ShoppingListItemRequestDtoMapperTest {
    @InjectMocks
    private ShoppingListItemRequestDtoMapper shoppingListItemRequestDtoMapper;

    @Test
    void convertTest() {
        ShoppingListItemRequestDto shoppingListItemRequestDto = new ShoppingListItemRequestDto(1L);
        UserShoppingListItem userShoppingListItem =
            shoppingListItemRequestDtoMapper.convert(shoppingListItemRequestDto);

        assertEquals(ShoppingListItem.builder().id(shoppingListItemRequestDto.getId()).build(),
            userShoppingListItem.getShoppingListItem());
        assertEquals(ShoppingListItemStatus.ACTIVE, userShoppingListItem.getStatus());
    }
}

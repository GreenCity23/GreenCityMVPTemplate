package greencity.mapping;

import greencity.dto.shoppinglistitem.ShoppingListItemDto;
import greencity.entity.localization.ShoppingListItemTranslation;
import greencity.enums.ShoppingListItemStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static greencity.ModelUtils.getShoppingListItem;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ShoppingListItemDtoMapperTest {
    @InjectMocks
    ShoppingListItemDtoMapper shoppingListItemDtoMapper;

    @Test
    void convertTest() {
        ShoppingListItemTranslation shoppingListItemTranslation = new ShoppingListItemTranslation();
        shoppingListItemTranslation.setShoppingListItem(getShoppingListItem());

        ShoppingListItemDto shoppingListItemDto = shoppingListItemDtoMapper.convert(shoppingListItemTranslation);

        assertEquals(shoppingListItemTranslation.getShoppingListItem().getId(), shoppingListItemDto.getId());
        assertEquals(shoppingListItemTranslation.getContent(), shoppingListItemDto.getText());
        assertEquals(ShoppingListItemStatus.ACTIVE.toString(), shoppingListItemDto.getStatus());
    }
}

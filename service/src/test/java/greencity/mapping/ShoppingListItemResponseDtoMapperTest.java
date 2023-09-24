package greencity.mapping;

import greencity.dto.shoppinglistitem.ShoppingListItemResponseDto;
import greencity.dto.shoppinglistitem.ShoppingListItemTranslationDTO;
import greencity.entity.ShoppingListItem;
import greencity.entity.localization.ShoppingListItemTranslation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static greencity.ModelUtils.getShoppingListItem;
import static greencity.ModelUtils.getShoppingListItemTranslations;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Arthur Mkrtchian
 */
class ShoppingListItemResponseDtoMapperTest {

    ShoppingListItemResponseDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ShoppingListItemResponseDtoMapper();
    }

    @Test
    @DisplayName("Convert ShoppingListItem to ShoppingListItemResponseDto test")
    void convert() {
        ShoppingListItem shoppingListItem = getShoppingListItem();

        ShoppingListItemResponseDto expected = new ShoppingListItemResponseDto()
            .setId(1L)
            .setTranslations(convertAllToDtos(getShoppingListItemTranslations()));

        ShoppingListItemResponseDto actual = mapper.convert(shoppingListItem);
        assertEquals(expected, actual);

    }

    public ShoppingListItemTranslationDTO convertToDto(ShoppingListItemTranslation translation) {
        return ShoppingListItemTranslationDTO.builder()
            .id(translation.getId())
            .content(translation.getContent())
            .build();
    }

    public List<ShoppingListItemTranslationDTO> convertAllToDtos(List<ShoppingListItemTranslation> translations) {
        return translations.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }
}

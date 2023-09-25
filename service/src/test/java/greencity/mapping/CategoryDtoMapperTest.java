package greencity.mapping;

import greencity.dto.category.CategoryDto;
import greencity.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDtoMapperTest {
    private CategoryDtoMapper categoryDtoMapper;

    @BeforeEach
    public void setUp() {
        categoryDtoMapper = new CategoryDtoMapper();
    }

    @Test
    @DisplayName("CategoryDto convert to Category test")
    void testConvert() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Test Name");

        Category category = categoryDtoMapper.convert(categoryDto);

        assertEquals("Test Name", category.getName());
    }
}
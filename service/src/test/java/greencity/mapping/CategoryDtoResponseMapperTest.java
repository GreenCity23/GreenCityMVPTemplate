package greencity.mapping;

import greencity.dto.category.CategoryDtoResponse;
import greencity.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDtoResponseMapperTest {

    private CategoryDtoResponseMapper categoryDtoResponseMapper;

    @BeforeEach
    public void setUp() {
        categoryDtoResponseMapper = new CategoryDtoResponseMapper();
    }

    @Test
    @DisplayName("Category convert to CategoryDtoResponse test")
    public void testConvert() {
        Category category = new Category().setId(1L).setName("Test name");

        CategoryDtoResponse categoryDtoResponse = categoryDtoResponseMapper.convert(category);

        assertEquals(1L, categoryDtoResponse.getId());
        assertEquals("Test name", categoryDtoResponse.getName());
    }
}
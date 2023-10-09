package greencity.mapping;

import greencity.dto.search.SearchNewsDto;
import greencity.entity.EcoNews;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static greencity.ModelUtils.getEcoNews;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class SearchNewsDtoMapperTest {
    @InjectMocks
    private SearchNewsDtoMapper searchNewsDtoMapper;

    @Test
    void convertTest() {
        EcoNews ecoNews = getEcoNews();
        SearchNewsDto searchNewsDto = searchNewsDtoMapper.convert(ecoNews);

        assertEquals(ecoNews.getId(), searchNewsDto.getId());
        assertEquals(ecoNews.getTitle(), searchNewsDto.getTitle());
        assertEquals(ecoNews.getAuthor().getId(), searchNewsDto.getAuthor().getId());
        assertEquals(ecoNews.getAuthor().getName(), searchNewsDto.getAuthor().getName());
        assertEquals(ecoNews.getCreationDate(), searchNewsDto.getCreationDate());
    }
}

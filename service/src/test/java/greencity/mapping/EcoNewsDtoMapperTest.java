package greencity.mapping;

import greencity.ModelUtils;
import greencity.constant.AppConstant;
import greencity.dto.econews.EcoNewsDto;
import greencity.dto.user.EcoNewsAuthorDto;
import greencity.entity.EcoNews;
import greencity.entity.localization.TagTranslation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

class EcoNewsDtoMapperTest {
    private final EcoNewsDtoMapper mapper = new EcoNewsDtoMapper();

    @Test
    void testConvertEcoNewsToEcoNewsDto() {

        EcoNews ecoNews = ModelUtils.getEcoNews();

        EcoNewsDto expected = EcoNewsDto.builder()
            .id(ecoNews.getId())
            .creationDate(ecoNews.getCreationDate())
            .imagePath(ecoNews.getImagePath())
            .title(ecoNews.getTitle())
            .content(ecoNews.getText())
            .shortInfo(ecoNews.getShortInfo())
            .author(EcoNewsAuthorDto.builder()
                .id(ecoNews.getAuthor().getId())
                .name(ecoNews.getAuthor().getName())
                .build())
            .tags(ecoNews.getTags().stream()
                .flatMap(t -> t.getTagTranslations().stream())
                .filter(t -> t.getLanguage().getCode().equals(AppConstant.DEFAULT_LANGUAGE_CODE))
                .map(TagTranslation::getName)
                .collect(Collectors.toList()))
            .tagsUa(ecoNews.getTags().stream()
                .flatMap(t -> t.getTagTranslations().stream())
                .filter(t -> t.getLanguage().getCode().equals("ua"))
                .map(TagTranslation::getName)
                .collect(Collectors.toList()))
            .likes(ecoNews.getUsersLikedNews().size())
            .dislikes(ecoNews.getUsersDislikedNews().size())
            .countComments((int) ecoNews.getEcoNewsComments().stream().filter(deleted -> !deleted.isDeleted()).count())
            .build();

        EcoNewsDto actual = mapper.convert(ecoNews);
        Assertions.assertEquals(expected, actual);
    }
}

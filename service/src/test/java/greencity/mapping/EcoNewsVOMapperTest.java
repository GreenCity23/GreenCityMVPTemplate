package greencity.mapping;

import greencity.ModelUtils;
import greencity.dto.econews.EcoNewsVO;
import greencity.dto.econewscomment.EcoNewsCommentVO;
import greencity.dto.language.LanguageVO;
import greencity.dto.tag.TagTranslationVO;
import greencity.dto.tag.TagVO;

import greencity.dto.user.UserVO;
import greencity.entity.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class EcoNewsVOMapperTest {
    private final EcoNewsVOMapper mapper = new EcoNewsVOMapper();

    @Test
    void testConvertEcoNewsToEcoNewsVO() {
        EcoNewsComment ecoNewsComment = ModelUtils.getEcoNewsComment();
        EcoNews ecoNews = ModelUtils.getEcoNews();
        UserVO userVO = ModelUtils.getUserVO();
        User user = ModelUtils.getUser();
        ecoNews.setUsersLikedNews(Collections.singleton(user));
        ecoNews.setUsersDislikedNews(Collections.singleton(user));
        ecoNews.setEcoNewsComments(List.of(ecoNewsComment));

        EcoNewsVO expected = EcoNewsVO.builder()
            .id(ecoNews.getId())
            .author(UserVO.builder()
                .id(userVO.getId())
                .name(userVO.getName())
                .role(userVO.getRole())
                .build())
            .creationDate(ecoNews.getCreationDate())
            .imagePath(ecoNews.getImagePath())
            .source(ecoNews.getSource())
            .text(ecoNews.getText())
            .title(ecoNews.getTitle())
            .tags(Collections.singletonList(
                TagVO.builder()
                    .id(1L)
                    .tagTranslations(Collections.singletonList(
                        TagTranslationVO.builder()
                            .name("Новини")
                            .id(1L)
                            .languageVO(LanguageVO.builder()
                                .code("ua")
                                .id(1L)
                                .build())
                            .build()))
                    .build()))
            .ecoNewsComments(Collections.singletonList(
                EcoNewsCommentVO.builder()
                    .id(ecoNewsComment.getId())
                    .createdDate(ecoNewsComment.getCreatedDate())
                    .currentUserLiked(ecoNewsComment.isCurrentUserLiked())
                    .deleted(ecoNewsComment.isDeleted())
                    .text(ecoNewsComment.getText())
                    .modifiedDate(ecoNewsComment.getModifiedDate())
                    .user(UserVO.builder()
                        .id(ecoNewsComment.getUser().getId())
                        .name(ecoNewsComment.getUser().getName())
                        .userStatus(ecoNewsComment.getUser().getUserStatus())
                        .role(ecoNewsComment.getUser().getRole())
                        .build())
                    .build()))
            .usersLikedNews(ecoNews.getUsersLikedNews().stream()
                .map(userLiked -> UserVO.builder()
                    .id(userLiked.getId())
                    .build())
                .collect(Collectors.toSet()))
            .usersDislikedNews(ecoNews.getUsersDislikedNews().stream()
                .map(userDisliked -> UserVO.builder()
                    .id(userDisliked.getId())
                    .build())
                .collect(Collectors.toSet()))
            .build();

        EcoNewsVO actual = mapper.convert(ecoNews);

        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getAuthor().getId(), actual.getAuthor().getId());
        Assertions.assertEquals(expected.getCreationDate(), actual.getCreationDate());
        Assertions.assertEquals(expected.getImagePath(), actual.getImagePath());
        Assertions.assertEquals(expected.getSource(), actual.getSource());
        Assertions.assertEquals(expected.getTitle(), actual.getTitle());
        Assertions.assertEquals(expected.getText(), actual.getText());
        Assertions.assertEquals(expected.getTags().size(), actual.getTags().size());
        Assertions.assertEquals(expected.getEcoNewsComments(), actual.getEcoNewsComments());
        Assertions.assertEquals(expected.getUsersLikedNews(), actual.getUsersLikedNews());
        Assertions.assertEquals(expected.getUsersDislikedNews(), actual.getUsersDislikedNews());

    }
}

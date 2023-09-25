package greencity.mapping;

import greencity.ModelUtils;
import greencity.dto.econews.EcoNewsVO;
import greencity.dto.econewscomment.EcoNewsCommentVO;
import greencity.dto.user.UserVO;
import greencity.entity.EcoNewsComment;
import greencity.entity.User;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EcoNewsCommentVOMapperTest {

    private final EcoNewsCommentVOMapper mapper = new EcoNewsCommentVOMapper();

    @Test
    void convertWithParentComment() {

        User user = ModelUtils.getUser();
        EcoNewsComment parentComment = ModelUtils.getEcoNewsComment();
        parentComment.setUsersLiked(new HashSet<>());
        parentComment.setId(2L);
        EcoNewsComment ecoNewsComment = ModelUtils.getEcoNewsComment();
        ecoNewsComment.setUsersLiked(Collections.singleton(user));
        ecoNewsComment.setParentComment(parentComment);

        EcoNewsCommentVO expected = EcoNewsCommentVO.builder()
            .id(ecoNewsComment.getId())
            .createdDate(ecoNewsComment.getCreatedDate())
            .text(ecoNewsComment.getText())
            .modifiedDate(ecoNewsComment.getModifiedDate())
            .currentUserLiked(ecoNewsComment.isCurrentUserLiked())
            .user(UserVO.builder()
                .id(user.getId())
                .name(user.getName())
                .role(user.getRole())
                .build())
            .usersLiked(ecoNewsComment.getUsersLiked().stream().map(userLiked -> UserVO.builder()
                .id(userLiked.getId())
                .build())
                .collect(Collectors.toSet()))
            .ecoNews(EcoNewsVO.builder()
                .id(ecoNewsComment.getEcoNews().getId())
                .build())
            .parentComment(EcoNewsCommentVO.builder()
                .id(parentComment.getId())
                .createdDate(parentComment.getCreatedDate())
                .text(parentComment.getText())
                .modifiedDate(parentComment.getModifiedDate())
                .currentUserLiked(parentComment.isCurrentUserLiked())
                .user(UserVO.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .role(user.getRole())
                    .build())
                .usersLiked(Collections.emptySet())
                .ecoNews(EcoNewsVO.builder()
                    .id(ecoNewsComment.getEcoNews().getId())
                    .build())
                .build())
            .build();

        EcoNewsCommentVO actual = mapper.convert(ecoNewsComment);
        assertEquals(expected, actual);
    }
}

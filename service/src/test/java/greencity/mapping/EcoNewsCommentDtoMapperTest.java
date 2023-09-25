package greencity.mapping;

import greencity.ModelUtils;
import greencity.dto.econewscomment.EcoNewsCommentAuthorDto;
import greencity.dto.econewscomment.EcoNewsCommentDto;
import greencity.entity.EcoNewsComment;
import greencity.enums.CommentStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;

class EcoNewsCommentDtoMapperTest {

    private final EcoNewsCommentDtoMapper mapper = new EcoNewsCommentDtoMapper();

    @Test
    void convertOriginalComment() {
        EcoNewsComment originalComment = ModelUtils.getEcoNewsComment();
        originalComment.setModifiedDate(originalComment.getCreatedDate());
        originalComment.setUsersLiked(new HashSet<>());

        EcoNewsCommentDto dtoOriginalComment = mapper.convert(originalComment);
        EcoNewsCommentDto expected = EcoNewsCommentDto.builder()
            .id(originalComment.getId())
            .modifiedDate(originalComment.getModifiedDate())
            .text(originalComment.getText())
            .status(CommentStatus.ORIGINAL)
            .likes(originalComment.getUsersLiked().size())
            .currentUserLiked(originalComment.isCurrentUserLiked()).build();
        expected.setAuthor(EcoNewsCommentAuthorDto.builder()
            .id(originalComment.getUser().getId())
            .name(originalComment.getUser().getName())
            .userProfilePicturePath(originalComment.getUser().getProfilePicturePath())
            .build());

        Assertions.assertEquals(dtoOriginalComment, expected);
    }

    @Test
    void convertEditedComment() {

        EcoNewsComment editedComment = ModelUtils.getEcoNewsComment();
        editedComment.setUsersLiked(new HashSet<>());
        editedComment.setModifiedDate(LocalDateTime.now().plusMinutes(30));

        EcoNewsCommentDto dto = mapper.convert(editedComment);
        Assertions.assertEquals(CommentStatus.EDITED, dto.getStatus());
        Assertions.assertEquals(editedComment.getText(), dto.getText());
        Assertions.assertEquals(editedComment.getModifiedDate(), dto.getModifiedDate());
    }

    @Test
    void convertDeletedComment() {

        EcoNewsComment deletedComment = ModelUtils.getEcoNewsComment();
        deletedComment.setUsersLiked(new HashSet<>());
        deletedComment.setDeleted(true);

        EcoNewsCommentDto dto = mapper.convert(deletedComment);

        Assertions.assertEquals(CommentStatus.DELETED, dto.getStatus());
        Assertions.assertEquals(deletedComment.getId(), dto.getId());
        Assertions.assertEquals(deletedComment.getModifiedDate(), dto.getModifiedDate());
    }
}

package greencity.mapping;

import greencity.dto.eventcomments.EventCommentAuthorDto;
import greencity.dto.eventcomments.EventCommentDto;
import greencity.entity.EventComment;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class EventCommentDtoMapper extends AbstractConverter<EventComment, EventCommentDto> {
    @Override
    protected EventCommentDto convert(EventComment eventComment) {
        return EventCommentDto.builder()
                .id(eventComment.getId())
                .text(eventComment.getText())
                .author(EventCommentAuthorDto.builder()
                        .id(eventComment.getUser().getId())
                        .userProfilePicturePath(eventComment.getUser().getProfilePicturePath())
                        .name(eventComment.getUser().getName())
                        .build())
                .modifiedDate(eventComment.getModifiedDate())
                .createdDate(eventComment.getCreatedDate())
                .likes(eventComment.getUsersLiked() == null ? 0 : eventComment.getUsersLiked().size())
                .replies(eventComment.getReplies() == null ? 0 : eventComment.getReplies().size())
                .build();
    }
}

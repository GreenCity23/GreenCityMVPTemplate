package greencity.mapping;

import greencity.dto.eventcomments.AddEventCommentDtoResponse;
import greencity.dto.eventcomments.EventCommentAuthorDto;
import greencity.entity.EventComment;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class AddEventCommentDtoResponseMapper extends AbstractConverter<EventComment, AddEventCommentDtoResponse> {
    @Override
    protected AddEventCommentDtoResponse convert(EventComment eventComment) {
        return AddEventCommentDtoResponse.builder()
            .id(eventComment.getId())
            .text(eventComment.getText())
            .modifiedDate(eventComment.getModifiedDate())
            .author(EventCommentAuthorDto.builder()
                .id(eventComment.getUser().getId())
                .name(eventComment.getUser().getName())
                .userProfilePicturePath(eventComment.getUser().getProfilePicturePath())
                .build())
            .build();
    }
}

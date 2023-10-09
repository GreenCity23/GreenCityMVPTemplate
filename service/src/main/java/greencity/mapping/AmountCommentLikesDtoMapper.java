package greencity.mapping;

import greencity.dto.eventcomments.AmountCommentLikesDto;
import greencity.entity.EventComment;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class AmountCommentLikesDtoMapper extends AbstractConverter<EventComment, AmountCommentLikesDto> {
    @Override
    protected AmountCommentLikesDto convert(EventComment eventComment) {
        return AmountCommentLikesDto.builder()
            .id(eventComment.getId())
            .liked(!eventComment.getUsersLiked().isEmpty())
            .amountLikes(eventComment.getUsersLiked().size())
            .userId(eventComment.getUser().getId())
            .build();
    }
}

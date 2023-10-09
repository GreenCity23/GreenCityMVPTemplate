package greencity.mapping;

import greencity.dto.eventcomments.AddEventCommentDtoRequest;
import greencity.entity.EventComment;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class AddEventCommentDtoRequestMapper extends AbstractConverter<AddEventCommentDtoRequest, EventComment> {
    @Override
    protected EventComment convert(AddEventCommentDtoRequest addEventCommentDtoRequest) {
        return EventComment.builder()
            .text(addEventCommentDtoRequest.getText())
            .build();
    }
}

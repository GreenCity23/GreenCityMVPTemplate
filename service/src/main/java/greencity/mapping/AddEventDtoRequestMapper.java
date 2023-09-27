package greencity.mapping;

import greencity.dto.event.AddEventDtoRequest;
import greencity.entity.Event;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class AddEventDtoRequestMapper extends AbstractConverter<AddEventDtoRequest, Event> {
    @Override
    protected Event convert(AddEventDtoRequest addEventDtoRequest) {
        return Event.builder()
                .title(addEventDtoRequest.getTitle())
                .description(addEventDtoRequest.getDescription())
                .eventClosed(!Boolean.parseBoolean(addEventDtoRequest.getOpen()))
                .build();
    }
}

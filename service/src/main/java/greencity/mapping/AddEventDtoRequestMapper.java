package greencity.mapping;

import greencity.dto.event.AddEventDtoRequest;
import greencity.entity.Event;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

/**
 * Class that used by {@link ModelMapper} to map {@link AddEventDtoRequest} into
 * {@link Event}.
 */
@Component
public class AddEventDtoRequestMapper extends AbstractConverter<AddEventDtoRequest, Event> {
    /**
     * Method for converting {@link AddEventDtoRequest} into {@link Event}.
     *
     * @param addEventDtoRequest object to convert.
     * @return converted object.
     */
    @Override
    protected Event convert(AddEventDtoRequest addEventDtoRequest) {
        return Event.builder()
            .title(addEventDtoRequest.getTitle())
            .description(addEventDtoRequest.getDescription())
            .eventClosed(!Boolean.parseBoolean(addEventDtoRequest.getOpen()))
            .creationDate(ZonedDateTime.now())
            .build();
    }
}

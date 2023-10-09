package greencity.mapping;

import greencity.dto.event.EventVO;
import greencity.dto.user.UserVO;
import greencity.entity.Event;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Class that used by {@link ModelMapper} to map {@link Event} into
 * {@link EventVO}.
 */
@Component
public class EventVOMapper extends AbstractConverter<Event, EventVO> {
    /**
     * Method for converting {@link Event} into {@link EventVO}.
     *
     * @param event object ot convert.
     * @return converted object.
     */
    @Override
    protected EventVO convert(Event event) {
        return EventVO.builder()
            .id(event.getId())
            .title(event.getTitle())
            .description(event.getDescription())
            .organizer(UserVO.builder()
                .id(event.getOrganizer().getId())
                .name(event.getOrganizer().getName())
                .userStatus(event.getOrganizer().getUserStatus())
                .role(event.getOrganizer().getRole())
                .build())
            .titleImage(event.getTitleImage())
            .build();
    }
}

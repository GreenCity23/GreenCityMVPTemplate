package greencity.mapping;

import greencity.dto.event.EventDto;
import greencity.entity.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static greencity.ModelUtils.getEvent;
import static greencity.ModelUtils.getEventDto;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class EventDtoMapperTest {

    @InjectMocks
    private EventDtoMapper eventDtoMapper;
    private EventDto expected = getEventDto();

    @Test
    void convert() {
        Event event = getEvent();
        EventDto actual = eventDtoMapper.convert(event);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getDateLocations(), actual.getDateLocations());
        assertEquals(expected.getOrganizer(), actual.getOrganizer());
        assertEquals(expected.getAdditionalImages(), actual.getAdditionalImages());
        assertEquals(expected.getCreationDate(), actual.getCreationDate());
        assertEquals(expected.isOpen(), actual.isOpen());
        assertEquals(expected.isFavorite(), actual.isFavorite());
        assertEquals(expected.isSubscribed(), actual.isSubscribed());
    }
}
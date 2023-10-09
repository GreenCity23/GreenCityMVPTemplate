package greencity.mapping;

import greencity.dto.event.EventVO;
import greencity.entity.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static greencity.ModelUtils.getEvent;
import static greencity.ModelUtils.getEventVO;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class EventVOMapperTest {

    @InjectMocks
    private EventVOMapper eventVOMapper;
    private final EventVO expected = getEventVO();

    @Test
    void convert() {
        Event event = getEvent();
        EventVO actual = eventVOMapper.convert(event);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getOrganizer(), actual.getOrganizer());
        assertEquals(expected.getTitleImage(), actual.getTitleImage());
    }
}

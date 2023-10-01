package greencity.mapping;

import greencity.ModelUtils;
import greencity.dto.event.AddEventDtoRequest;
import greencity.entity.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AddEventDtoRequestMapperTest {
    @InjectMocks
    private AddEventDtoRequestMapper mapper;

    @Test
    void convert() {
        AddEventDtoRequest expected = ModelUtils.getAddEventDtoRequest();
        Event actual = mapper.convert(expected);

        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(Boolean.parseBoolean(expected.getOpen()), !actual.isEventClosed());
    }
}
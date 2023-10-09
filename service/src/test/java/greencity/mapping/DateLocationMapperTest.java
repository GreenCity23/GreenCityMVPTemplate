package greencity.mapping;

import greencity.dto.event.EventDateLocationDto;
import greencity.entity.DateLocation;
import greencity.service.GeocodingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static greencity.ModelUtils.getDateLocation;
import static greencity.ModelUtils.getEventDateLocationDto;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class DateLocationMapperTest {
    @InjectMocks
    private DateLocationMapper dateLocationMapper;
    @Mock
    private GeocodingService geocodingService;
    private DateLocation expected = getDateLocation();

    @Test
    void convert() {
        EventDateLocationDto eventDateLocationDto = getEventDateLocationDto();
        DateLocation actual = dateLocationMapper.convert(eventDateLocationDto);

        assertEquals(expected, actual);
    }
}
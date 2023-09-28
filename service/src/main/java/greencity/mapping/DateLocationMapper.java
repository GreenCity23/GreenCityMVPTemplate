package greencity.mapping;

import greencity.dto.event.EventDateLocationDto;
import greencity.entity.Address;
import greencity.entity.DateLocation;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Class that used by {@link ModelMapper} to map {@link EventDateLocationDto}
 * into {@link DateLocation}.
 */
@Component
public class DateLocationMapper extends AbstractConverter<EventDateLocationDto, DateLocation> {

    /**
     * Method for converting {@link EventDateLocationDto} into {@link DateLocation}.
     *
     * @param eventDateLocationDto object to convert.
     * @return converted object.
     */
    @Override
    protected DateLocation convert(EventDateLocationDto eventDateLocationDto) {
        return DateLocation.builder()
                .startDate(eventDateLocationDto.getStartDate())
                .finishDate(eventDateLocationDto.getFinishDate())
                .address(Address.builder()
                        .latitude(eventDateLocationDto.getCoordinates().getLatitude())
                        .longitude(eventDateLocationDto.getCoordinates().getLongitude())
                        .build())
                .onlineLink(eventDateLocationDto.getOnlineLink())
                .build();
    }
}

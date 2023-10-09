package greencity.mapping;

import greencity.dto.event.EventDateLocationDto;
import greencity.entity.Address;
import greencity.entity.DateLocation;
import greencity.service.GeocodingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Class that used by {@link ModelMapper} to map {@link EventDateLocationDto}
 * into {@link DateLocation}.
 */
@Component
@RequiredArgsConstructor
public class DateLocationMapper extends AbstractConverter<EventDateLocationDto, DateLocation> {

    private final GeocodingService geocodingService;

    /**
     * Method for converting {@link EventDateLocationDto} into {@link DateLocation}.
     *
     * @param eventDateLocationDto object to convert.
     * @return converted object.
     */
    @Override
    protected DateLocation convert(EventDateLocationDto eventDateLocationDto) {
        Map<String, String> addresses = geocodingService.getAddress(eventDateLocationDto.getCoordinates().getLatitude(),
            eventDateLocationDto.getCoordinates().getLongitude());
        return DateLocation.builder()
            .id(eventDateLocationDto.getId())
            .startDate(eventDateLocationDto.getStartDate())
            .finishDate(eventDateLocationDto.getFinishDate())
            .address(Address.builder()
                .cityEn(addresses.get("cityEn"))
                .cityUa(addresses.get("cityUa"))
                .countryEn(addresses.get("countryEn"))
                .countryUa(addresses.get("countryUa"))
                .formattedAddressEn(addresses.get("formattedAddressEn"))
                .formattedAddressUa(addresses.get("formattedAddressUa"))
                .houseNumber(addresses.get("houseNumber"))
                .latitude(eventDateLocationDto.getCoordinates().getLatitude())
                .longitude(eventDateLocationDto.getCoordinates().getLongitude())
                .regionEn(addresses.get("regionEn"))
                .regionUa(addresses.get("regionUa"))
                .streetEn(addresses.get("streetEn"))
                .streetUa(addresses.get("streetUa"))
                .build())
            .onlineLink(eventDateLocationDto.getOnlineLink())
            .build();
    }
}

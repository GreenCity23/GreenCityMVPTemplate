package greencity.mapping;

import greencity.dto.event.*;
import greencity.dto.tag.TagUaEnDto;
import greencity.entity.Event;
import lombok.AllArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;



/**
 * Class that used by {@link ModelMapper} to map {@link Event} into
 * {@link EventDto}.
 */
@Component
@AllArgsConstructor
public class EventDtoMapper extends AbstractConverter<Event, EventDto> {

    /**
     * Method for converting {@link Event} into {@link EventDto}.
     *
     * @param event object ot convert.
     * @return converted object.
     */
    @Override
    protected EventDto convert(Event event) {
        return EventDto.builder()
            .id(event.getId())
            .title(event.getTitle())
            .titleImage(event.getTitleImage())
            .creationDate(event.getCreationDate())
            .description(event.getDescription())
            .dateLocations(event.getDateLocations()
                .stream()
                .map(dateLocation -> EventDateLocationDto.builder()
                    .id(dateLocation.getId())
                    .coordinates(AddressDto.builder()
                        .cityEn(dateLocation.getAddress().getCityEn())
                        .cityUa(dateLocation.getAddress().getCityUa())
                        .countryEn(dateLocation.getAddress().getCountryEn())
                        .countryUa(dateLocation.getAddress().getCountryUa())
                        .formattedAddressEn(dateLocation.getAddress().getFormattedAddressEn())
                        .formattedAddressUa(dateLocation.getAddress().getFormattedAddressUa())
                        .houseNumber(dateLocation.getAddress().getHouseNumber())
                        .latitude(dateLocation.getAddress().getLatitude())
                        .longitude(dateLocation.getAddress().getLongitude())
                        .regionUa(dateLocation.getAddress().getRegionUa())
                        .regionEn(dateLocation.getAddress().getRegionEn())
                        .streetEn(dateLocation.getAddress().getStreetEn())
                        .streetUa(dateLocation.getAddress().getStreetUa())
                        .build())
                    .startDate(dateLocation.getStartDate())
                    .finishDate(dateLocation.getFinishDate())
                    .onlineLink(dateLocation.getOnlineLink() == null ? ""
                        : dateLocation.getOnlineLink())
                    .build())
                .collect(Collectors.toList()))
            .isFavorite(event.isFavorite())
            .isSubscribed(event.isSubscribed())
            .open(!event.isEventClosed())
            .organizer(EventAuthorDto.builder()
                .id(event.getOrganizer().getId())
                .name(event.getOrganizer().getName())
                .organizerRating(event.getOrganizer().getEventOrganizerRating() == null ? 0.00
                    : event.getOrganizer().getEventOrganizerRating())
                .build())
            .additionalImages(event.getAdditionalImages())
            .tags(event.getTags()
                .stream()
                .map(tag -> {
                    TagUaEnDto tagUaEnDto = new TagUaEnDto();
                    tagUaEnDto.setId(tag.getId());
                    tagUaEnDto.setNameUa(tag.getTagTranslations().stream()
                        .filter(tagTranslation -> tagTranslation.getLanguage().getCode().equals("ua"))
                        .findFirst()
                        .get()
                        .getName());
                    tagUaEnDto.setNameEn(tag.getTagTranslations().stream()
                        .filter(tagTranslation -> tagTranslation.getLanguage().getCode().equals("en"))
                        .findFirst()
                        .get()
                        .getName());
                    return tagUaEnDto;
                })
                .collect(Collectors.toSet()))
            .build();
    }
}

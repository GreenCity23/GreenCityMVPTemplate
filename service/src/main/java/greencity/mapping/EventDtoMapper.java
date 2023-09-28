package greencity.mapping;

import greencity.dto.event.EventAuthorDto;
import greencity.dto.event.EventDto;
import greencity.dto.event.TagUaEnDto;
import greencity.entity.Event;
import greencity.repository.TagTranslationRepo;
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

    private final TagTranslationRepo tagTranslationRepo;
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
                .isFavorite(event.isFavorite())
                .isSubscribed(event.isSubscribed())
                .open(!event.isEventClosed())
                .organizer(EventAuthorDto.builder()
                        .id(event.getOrganizer().getId())
                        .name(event.getOrganizer().getName())
                        .organizerRating(event.getOrganizer().getEventOrganizerRating() == null ? 0.00 :
                                event.getOrganizer().getEventOrganizerRating())
                        .build())
                .tags(event.getTags()
                        .stream()
                        .map(tag -> {
                            TagUaEnDto tagUaEnDto = new TagUaEnDto();
                            tagUaEnDto.setId(tag.getId());
                            tagUaEnDto.setNameUa(tag.getTagTranslations().get(0).getName());
                            tagUaEnDto.setNameEn(tag.getTagTranslations().get(1).getName());
                            return tagUaEnDto;
                        })
                        .collect(Collectors.toSet()))
                .build();
    }
}

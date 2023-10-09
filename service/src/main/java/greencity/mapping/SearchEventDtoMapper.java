package greencity.mapping;

import greencity.dto.event.EventAuthorDto;
import greencity.dto.event.SearchEventDto;
import greencity.entity.Event;
import greencity.entity.Tag;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class SearchEventDtoMapper extends AbstractConverter<Event, SearchEventDto> {
    @Override
    protected SearchEventDto convert(Event event) {
        return SearchEventDto.builder()
            .id(event.getId())
            .title(event.getTitle())
            .author(EventAuthorDto.builder()
                .id(event.getOrganizer().getId())
                .name(event.getOrganizer().getName())
                .organizerRating(event.getOrganizer().getEventOrganizerRating() == null ? 0.0
                    : event.getOrganizer().getEventOrganizerRating())
                .build())
            .creationDate(event.getCreationDate())
            .tags(event.getTags().stream()
                .map(Tag::toString).collect(Collectors.toList()))
            .build();
    }
}

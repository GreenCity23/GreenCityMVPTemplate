package greencity.service;

import greencity.dto.PageableAdvancedDto;
import greencity.dto.event.AddEventDtoRequest;
import greencity.dto.event.EventDto;
import greencity.dto.user.UserVO;
import org.springframework.data.domain.Pageable;

public interface EventService {

    EventDto save(AddEventDtoRequest addEventDtoRequest);

    EventDto findById(Long eventId);

    PageableAdvancedDto<EventDto> findAll(Pageable page);

    EventDto update(EventDto eventDto);

    void delete(Long id, UserVO user);
}

package greencity.service;

import greencity.dto.PageableAdvancedDto;
import greencity.dto.event.AddEventDtoRequest;
import greencity.dto.event.EventDto;
import greencity.dto.user.UserVO;
import greencity.repository.EventRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepo eventRepo;

    @Override
    public EventDto save(AddEventDtoRequest addEventDtoRequest) {
        return null;
    }

    @Override
    public EventDto findById(Long eventId) {
        return null;
    }

    @Override
    public PageableAdvancedDto<EventDto> findAll(Pageable page) {
        return null;
    }

    @Override
    public EventDto update(EventDto eventDto) {
        return null;
    }

    @Override
    public void delete(Long id, UserVO user) {

    }
}

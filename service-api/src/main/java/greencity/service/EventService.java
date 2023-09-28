package greencity.service;

import greencity.dto.PageableAdvancedDto;
import greencity.dto.event.AddEventDtoRequest;
import greencity.dto.event.EventDto;
import greencity.dto.user.UserVO;
import greencity.exception.exceptions.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface EventService {

    EventDto save(AddEventDtoRequest addEventDtoRequest, MultipartFile[] images, Long organizerId);

    EventDto findById(Long eventId);

    PageableAdvancedDto<EventDto> findAll(Pageable page);

    EventDto update(EventDto eventDto);

    void delete(Long id, UserVO user);

    /**
     * Method to upload events images.
     *
     * @param images - array of events images
     * @return array of images path
     */
    String[] uploadImages(MultipartFile[] images);
}

package greencity.service;

import greencity.dto.PageableAdvancedDto;
import greencity.dto.event.AddEventDtoRequest;
import greencity.dto.event.EventDto;
import greencity.dto.event.EventVO;
import greencity.dto.user.UserVO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface EventService {

    EventDto save(AddEventDtoRequest addEventDtoRequest, MultipartFile[] images, Long organizerId);

    /**
     * Method for getting the {@link EventDto} instance by its id.
     *
     * @param eventId {@link EventDto} instance id.
     * @return {@link EventDto} instance.
     */
    EventDto findById(Long eventId);

    /**
     * Method for getting all events by page.
     *
     * @return PageableDto of {@link EventDto} instances.
     */
    PageableAdvancedDto<EventDto> findAll(Pageable page);

    /**
     * Method for getting all events created by user by page.
     *
     * @param user organizer of events.
     * @param page parameters of to search.
     * @return PageableDto of {@link EventDto} instances.
     */
    PageableAdvancedDto<EventDto> findAllByUser(UserVO user, Pageable page);

    /**
     * Method for updating {@link EventVO} instance.
     *
     * @param eventVO - instance of {@link EventVO}.
     * @return instance of {@link EventDto};
     */
    EventDto update(EventVO eventVO, MultipartFile[] image, UserVO userId);

    /**
     * Method for deleting the {@link EventDto} instance by its id.
     *
     * @param id   - {@link EventDto} instance id which will be deleted.
     * @param user - current {@link UserVO} that wants to delete.
     */
    void delete(Long id, UserVO user);

    /**
     * Method to upload events images.
     *
     * @param images - array of events images
     * @return array of images path
     */
    String[] uploadImages(MultipartFile[] images);
}

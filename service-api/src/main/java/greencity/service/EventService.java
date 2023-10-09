package greencity.service;

import greencity.dto.PageableAdvancedDto;
import greencity.dto.PageableDto;
import greencity.dto.event.AddEventDtoRequest;
import greencity.dto.event.EventAttenderDto;
import greencity.dto.event.EventDto;
import greencity.dto.event.SearchEventDto;
import greencity.dto.user.UserVO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService {
    /**
     * Method for creating the Event instance.
     *
     * @param addEventDtoRequest dto for creating Event instance.
     * @param images             event images
     * @param organizerId        ID of event creator
     * @return {@link EventDto} instance.
     */
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
     * Method for updating Event instance.
     *
     * @param addEventDtoRequest - instance of {@link AddEventDtoRequest}.
     * @param images             event images.
     * @param organizerId        ID of event author.
     * @return {@link EventDto} instance.
     */
    EventDto update(AddEventDtoRequest addEventDtoRequest, MultipartFile[] images, Long organizerId);

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

    /**
     * Method that allow you to search {@link SearchEventDto}.
     *
     * @param pageable    {@link Pageable}.
     * @param searchQuery query to search.
     * @return PageableDto of {@link SearchEventDto} instances.
     */
    PageableDto<SearchEventDto> searchEvent(Pageable pageable, String searchQuery);

    /**
     * Method returns PageableAdvancedDto of {@link EventDto} by attender id and
     * page.
     *
     * @param attenderId {@link Long} attender id.
     * @param page       parameters of to search.
     * @return PageableAdvancedDto of {@link EventDto} instances.
     * @author Maksym Fartushok
     */
    PageableAdvancedDto<EventDto> findAllByAttenderId(Long attenderId, Pageable page);

    /**
     * Method returns PageableAdvancedDto of {@link EventDto} where user is
     * organizer or attender by page.
     *
     * @param userId {@link Long} attender id.
     * @param page   parameters of to search.
     * @return PageableAdvancedDto of {@link EventDto} instances.
     * @author Maksym Fartushok
     */
    PageableAdvancedDto<EventDto> findAllRelatedToUser(Long userId, Pageable page);

    /**
     * Method for adding attenders to event.
     *
     * @param eventId ID of the event.
     * @param attenderId {@link Long} attender id.
     */
    void addAttenderToEvent(Long eventId, Long attenderId);

    /**
     *  Method for getting all attenders of the event.
     *
     *  @param eventId ID of the event.
     */
    List<EventAttenderDto> getAllSubscribers(Long eventId);

    /**
     *  Method for remove attenders from the event.
     *
     *  @param eventId ID of the event.
     *  @param attenderId {@link Long} attender id.
     */
    void removeAttenderFromEvent(Long eventId, Long attenderId);

    /**
     * Method for adding event to favourites.
     *
     * @param eventId ID of the event to be added to favorites.
     * @author Maksym Fartushok
     */
    void addToFavorites(Long eventId);

    /**
     * Method for removing event from favourites.
     *
     * @param eventId ID of the event to be removed from favorites.
     * @author Maksym Fartushok
     */
    void removeFromFavorites(Long eventId);
}

package greencity.service;

import greencity.client.RestClient;
import greencity.constant.ErrorMessage;
import greencity.dto.PageableAdvancedDto;
import greencity.dto.event.*;
import greencity.dto.tag.TagVO;
import greencity.dto.user.PlaceAuthorDto;
import greencity.dto.user.UserVO;
import greencity.entity.DateLocation;
import greencity.entity.Event;
import greencity.entity.Tag;
import greencity.entity.User;
import greencity.enums.Role;
import greencity.enums.TagType;
import greencity.exception.exceptions.NotFoundException;
import greencity.exception.exceptions.NotSavedException;
import greencity.exception.exceptions.UnsupportedSortException;
import greencity.exception.exceptions.UserHasNoPermissionToAccessException;
import greencity.repository.EventRepo;
import greencity.repository.TagTranslationRepo;
import greencity.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static greencity.constant.AppConstant.AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepo eventRepo;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final TagsService tagService;
    private final TagTranslationRepo tagTranslationRepo;
    private final FileService fileService;
    private final HttpServletRequest httpServletRequest;
    private final RestClient restClient;

    private PageableAdvancedDto<EventDto> buildPageableAdvancedDto(Page<Event> eventPage) {
        List<EventDto> eventDtos = eventPage.stream()
                .map(event -> modelMapper.map(event, EventDto.class))
                .collect(Collectors.toList());

        return new PageableAdvancedDto<>(
                eventDtos,
                eventPage.getTotalElements(),
                eventPage.getPageable().getPageNumber(),
                eventPage.getTotalPages(),
                eventPage.getNumber(),
                eventPage.hasPrevious(),
                eventPage.hasNext(),
                eventPage.isFirst(),
                eventPage.isLast());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EventDto save(AddEventDtoRequest addEventDtoRequest, MultipartFile[] images, Long organizerId) {
        Event savedEvent = genericSave(addEventDtoRequest, images, organizerId);

        EventDto eventDto = modelMapper.map(savedEvent, EventDto.class);
        sendEmailDto(eventDto, savedEvent.getOrganizer());
        return eventDto;
    }

    private Event genericSave(AddEventDtoRequest addEventDtoRequest, MultipartFile[] images, Long organizerId) {
        User organizer = userRepo.findById(organizerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND_BY_ID + organizerId));

        Event eventToSave = modelMapper.map(addEventDtoRequest, Event.class);
        eventToSave.setOrganizer(organizer);

        if (addEventDtoRequest.getDatesLocations().size() > 7) {
            throw new NotSavedException(ErrorMessage.EVENT_INVALID_DURATION);
        }

        if (images.length != 0) {
            processEventImages(eventToSave, images);
        }

        Set<String> tagsSet = new HashSet<>(addEventDtoRequest.getTags());
        if (tagsSet.size() < addEventDtoRequest.getTags().size()) {
            throw new NotSavedException(ErrorMessage.EVENT_NOT_SAVED);
        }

        List<TagVO> tagVOS = tagService.findTagsWithAllTranslationsByNamesAndType(
                addEventDtoRequest.getTags(), TagType.EVENT);
        List<Tag> tags = modelMapper.map(tagVOS,
                new TypeToken<List<Tag>>() {
                }.getType());
        eventToSave.setTags(tags);

        eventToSave.setDateLocations(addEventDtoRequest.getDatesLocations().stream()
                .map(eventDateLocationDto -> {
                    if (eventDateLocationDto.getStartDate().isBefore(ZonedDateTime.now())) {
                        throw new NotSavedException(ErrorMessage.EVENT_PAST_CANNOT_BE_SAVED);
                    }
                    return modelMapper.map(eventDateLocationDto, DateLocation.class).setEvent(eventToSave);
                })
                .collect(Collectors.toList()));

        return eventRepo.save(eventToSave);
    }

    /**
     * Method for sending an email to user, when event was created
     *
     * @author Vladyslav Siverskyi.
     */
    public void sendEmailDto(EventDto eventDto, User user) {
        String accessToken = httpServletRequest.getHeader(AUTHORIZATION);
        PlaceAuthorDto placeAuthorDto = modelMapper.map(user, PlaceAuthorDto.class);
        EventForSendEmailDto eventForSendEmailDto = EventForSendEmailDto.builder()
                .title(eventDto.getTitle())
                .titleImage(eventDto.getTitleImage())
                .description(eventDto.getDescription())
                .creationDate(eventDto.getCreationDate())
                .open(eventDto.isOpen())
                .author(placeAuthorDto)
                .build();
        restClient.addEvent(eventForSendEmailDto, accessToken.substring(7));
    }

    /**
     * Method for getting the {@link EventDto} instance by its id.
     *
     * @param eventId {@link EventDto} instance id.
     * @return {@link EventDto} instance.
     */
    @Override
    public EventDto findById(Long eventId) {
        Event event = eventRepo
                .findById(eventId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUND_BY_ID + eventId));
        return modelMapper.map(event, EventDto.class);
    }

    /**
     * Method for getting all events by page.
     *
     * @return PageableDto of {@link EventDto} instances.
     */
    @Override
    public PageableAdvancedDto<EventDto> findAll(Pageable page) {
        Page<Event> pages;
        if (page.getSort().isEmpty()) {
            pages = eventRepo.findAllByOrderByCreationDateDesc(page);
        } else {
            if (page.getSort().isUnsorted()) {
                pages = eventRepo.findAll(page);
            } else {
                throw new UnsupportedSortException(ErrorMessage.INVALID_SORTING_VALUE);
            }
        }
        return buildPageableAdvancedDto(pages);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageableAdvancedDto<EventDto> findAllByUser(UserVO user, Pageable page) {
        User existingUser = userRepo.findById(user.getId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND_BY_ID + user.getId()));
        if (page.getSort().isEmpty()) {
            Page<Event> pages = eventRepo.findAllByOrganizerOrderByCreationDateDesc(existingUser, page);
            return buildPageableAdvancedDto(pages);
        } else {
            throw new UnsupportedSortException(ErrorMessage.INVALID_SORTING_VALUE);
        }
    }

    private boolean userIsOrganizerOrAdmin(User user, Event existingEvent) {
        return Objects.equals(user.getId(), existingEvent.getOrganizer().getId())
                || user.getRole().equals(Role.ROLE_ADMIN);
    }

    /**
     * {@inheritDoc}
     */

    @Override
    public EventDto update(EditEventDtoRequest editEventDtoRequest, MultipartFile[] images, Long organizerId) {
        Event updatedEvent = genericUpdate(editEventDtoRequest, images, organizerId);
        return modelMapper.map(updatedEvent, EventDto.class);
    }

    private Event genericUpdate(EditEventDtoRequest editEventDtoRequest, MultipartFile[] images, Long organizerId) {
        Event updatedEvent = eventRepo.findById(editEventDtoRequest.getId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUND_BY_ID + editEventDtoRequest.getId()));

        User organizer = userRepo.findById(organizerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND_BY_ID + organizerId));

        if (!userIsOrganizerOrAdmin(organizer, updatedEvent)) {
            throw new AccessDeniedException(ErrorMessage.IMPOSSIBLE_UPDATE_EVENT);
        }

        if (updatedEvent.getDateLocations().stream()
                .anyMatch(dateLocation -> dateLocation.getStartDate().isBefore(ZonedDateTime.now()))) {
            throw new AccessDeniedException(ErrorMessage.EVENT_PAST_CANNOT_BE_EDITED);
        }

        updatedEvent.setTitle(editEventDtoRequest.getTitle());
        updatedEvent.setDescription(editEventDtoRequest.getDescription());

        updatedEvent.setDateLocations(editEventDtoRequest.getDatesLocations().stream()
                .map(eventDateLocationDto -> {
                    if (eventDateLocationDto.getStartDate().isBefore(ZonedDateTime.now())) {
                        throw new NotSavedException(ErrorMessage.EVENT_PAST_CANNOT_BE_SAVED);
                    }
                    return modelMapper.map(eventDateLocationDto, DateLocation.class).setEvent(updatedEvent);
                })
                .collect(Collectors.toList()));

        List<TagVO> tagVOS = tagService.findTagsWithAllTranslationsByNamesAndType(
                editEventDtoRequest.getTags(), TagType.EVENT);
        List<Tag> tags = modelMapper.map(tagVOS,
                new TypeToken<List<Tag>>() {
                }.getType());
        updatedEvent.setTags(tags);

        if (images != null && images.length > 0) {
            processEventImages(updatedEvent, images);
        }

        updatedEvent.setEventClosed(Boolean.parseBoolean(editEventDtoRequest.getOpen()));

        return eventRepo.save(updatedEvent);
    }


    /**
     * Method for deleting the {@link EventDto} instance by its id.
     *
     * @param id   - {@link EventDto} instance id which will be deleted.
     * @param user - current {@link UserVO} that wants to delete.
     */
    @Override
    public void delete(Long id, UserVO user) {
        EventDto eventDto = findById(id);
        if (user.getRole() != Role.ROLE_ADMIN && !user.getId().equals(eventDto.getOrganizer().getId())) {
            throw new UserHasNoPermissionToAccessException(ErrorMessage.USER_HAS_NO_PERMISSION);
        }
        eventRepo.deleteById(id);
    }

    /**
     * Method to upload events images.
     *
     * @param images - array of events images
     * @return array of images path
     */
    @Override
    public String[] uploadImages(MultipartFile[] images) {
        return Arrays.stream(images).map(fileService::upload).toArray(String[]::new);
    }

    /**
     * Method to upload events images.
     *
     * @param eventToSave - event which should be created after adding images
     * @param images      - array of events images
     */
    private void processEventImages(Event eventToSave, MultipartFile[] images) {
        if (images.length > 5) {
            throw new IllegalArgumentException(ErrorMessage.USER_CANNOT_ADD_MORE_THAN_5_EVENT_IMAGES);
        }
        String[] paths = uploadImages(images);
        if (paths.length != images.length) {
            throw new NotSavedException(ErrorMessage.EVENT_NOT_SAVED);
        }
        eventToSave.setTitleImage(paths[0]);
        if (images.length > 1) {
            List<String> additionalImages = Arrays.stream(paths)
                    .skip(1)
                    .collect(Collectors.toList());
            eventToSave.setAdditionalImages(additionalImages);
        }
    }
}

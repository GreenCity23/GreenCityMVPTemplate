package greencity.service;

import greencity.client.RestClient;
import greencity.constant.ErrorMessage;
import greencity.dto.PageableAdvancedDto;
import greencity.dto.event.AddEventDtoRequest;
import greencity.dto.event.AddEventDtoRequest;
import greencity.dto.event.EventDateLocationDto;
import greencity.dto.event.EventDto;
import greencity.dto.event.EventForSendEmailDto;
import greencity.dto.tag.TagVO;
import greencity.dto.user.PlaceAuthorDto;
import greencity.dto.user.UserVO;
import greencity.entity.*;
import greencity.enums.Role;
import greencity.enums.TagType;
import greencity.exception.exceptions.*;
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
    private final UserService userService;

    /**
     * {@inheritDoc}
     */
    @Override
    public EventDto save(AddEventDtoRequest addEventDtoRequest, MultipartFile[] images, Long organizerId) {
        User organizer = userRepo.findById(organizerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND_BY_ID + organizerId));
        Event eventToSave = modelMapper.map(addEventDtoRequest, Event.class);
        eventToSave.setOrganizer(organizer);

        EventDto eventDto =
                modelMapper.map(genericSaveOrUpdate(eventToSave, addEventDtoRequest, images), EventDto.class);
        sendEmailDto(eventDto, organizer);
        return eventDto;
    }

    /**
     * {@inheritDoc}
     */
    public EventDto update(AddEventDtoRequest addEventDtoRequest, MultipartFile[] images, Long organizerId) {
        Event updatedEvent = eventRepo.findById(addEventDtoRequest.getId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUND_BY_ID + addEventDtoRequest.getId()));
        User organizer = userRepo.findById(organizerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND_BY_ID + organizerId));

        if (!userIsOrganizerOrAdmin(organizer, updatedEvent)) {
            throw new AccessDeniedException(ErrorMessage.IMPOSSIBLE_UPDATE_EVENT);
        }
        updatedEvent.setTitle(addEventDtoRequest.getTitle());
        updatedEvent.setDescription(addEventDtoRequest.getDescription());
        updatedEvent.setEventClosed(!Boolean.parseBoolean(addEventDtoRequest.getOpen()));
        EventDto eventDto = modelMapper.map(genericSaveOrUpdate(updatedEvent, addEventDtoRequest, images), EventDto.class);
        return eventDto;
    }

    private Event genericSaveOrUpdate(Event event, AddEventDtoRequest addEventDtoRequest, MultipartFile[] images) {
        validateDateLocations(event, addEventDtoRequest.getDatesLocations());
        validateTags(event, addEventDtoRequest.getTags());
        validateImages(event, images);

        return eventRepo.save(event);
    }

    private void validateImages(Event event, MultipartFile[] images) {
        if (images != null && images.length > 0) {
            processEventImages(event, images);
        }
    }

    private void validateDateLocations(Event event, List<EventDateLocationDto> dateLocations) {
        if (dateLocations.size() > 7 || dateLocations.isEmpty()) {
            throw new NotSavedException(ErrorMessage.EVENT_INVALID_DURATION);
        }

        event.setDateLocations(dateLocations.stream()
                .map(eventDateLocationDto -> {
                    ZonedDateTime startDate = eventDateLocationDto.getStartDate();
                    ZonedDateTime finishDate = eventDateLocationDto.getFinishDate();

                    if (startDate.isBefore(ZonedDateTime.now())) {
                        throw new NotSavedException(ErrorMessage.EVENT_PAST_CANNOT_BE_SAVED);
                    }

                    if (finishDate.isBefore(startDate)) {
                        throw new NotSavedException(ErrorMessage.INVALID_DATE_RANGE);
                    }

                    return modelMapper.map(eventDateLocationDto, DateLocation.class).setEvent(event);
                })
                .collect(Collectors.toList()));
    }

    private void validateTags(Event event, List<String> tags) {
        Set<String> tagsSet = new HashSet<>(tags);
        if (tagsSet.size() < tags.size()) {
            throw new NotSavedException(ErrorMessage.EVENT_NOT_SAVED);
        }

        List<TagVO> tagVOS = tagService.findTagsWithAllTranslationsByNamesAndType(tags, TagType.EVENT);
        List<Tag> eventTags = modelMapper.map(tagVOS, new TypeToken<List<Tag>>() {
        }.getType());
        event.setTags(eventTags);
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
        Page<Event> pages;
        userRepo.findById(user.getId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND_BY_ID + user.getId()));
        if (page.getSort().isEmpty()) {
            pages = eventRepo.findAllByOrganizerOrderByCreationDateDesc(modelMapper.map(user, User.class), page);
        } else {
            throw new UnsupportedSortException(ErrorMessage.INVALID_SORTING_VALUE);
        }
        return buildPageableAdvancedDto(pages);
    }

    private boolean userIsOrganizerOrAdmin(User user, Event existingEvent) {
        return Objects.equals(user.getId(), existingEvent.getOrganizer().getId())
                || user.getRole().equals(Role.ROLE_ADMIN);
    }

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
     * Method to rate an event.
     *
     * @param eventId - the ID of the event to rate
     * @param email - the email of the user rating the event
     * @param grade - the grade to assign to the event
     */
    @Override
    public void rateEvent(Long eventId, String email, Integer grade) {
        Event event =
                eventRepo.findById(eventId).orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUND));
        User currentUser = modelMapper.map(restClient.findByEmail(email), User.class);

        event.getEventGrades().add(EventGrade.builder().event(event).grade(grade).user(currentUser).build());
        eventRepo.save(event);

        userService.updateEventOrganizerRating(event.getOrganizer().getId(),
                calculateEventOrganizerRating(event.getOrganizer()));
    }

    private Double calculateEventOrganizerRating(User user) {
        double finalRating = 0;

        List<Event> events = eventRepo.getAllByOrganizer(user);

        if (events != null && !events.isEmpty()) {
            long totalGrades = events.stream()
                    .mapToLong(event -> event.getEventGrades().size())
                    .sum();

            if (totalGrades != 0) {
                double summaryGrade = events.stream()
                        .flatMap(event -> event.getEventGrades().stream())
                        .mapToDouble(EventGrade::getGrade)
                        .sum();

                finalRating = summaryGrade / totalGrades;
            }
        }

        return finalRating;
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
        for (MultipartFile image : images) {
            if (image.getSize() > 10 * 1024 * 1024) {
                throw new IllegalArgumentException(ErrorMessage.IMAGE_SIZE_EXCEEDS_10MB);
            }
            if (!isSupportedContentType(image.getContentType())) {
                throw new IllegalArgumentException(ErrorMessage.UNSUPPORTED_IMAGE_FORMAT);
            }
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

    private boolean isSupportedContentType(String contentType) {
        var supportedContents = List.of("image/jpg", "image/jpeg", "image/png");
        return supportedContents.contains(contentType);
    }
}

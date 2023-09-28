package greencity.service;

import greencity.constant.ErrorMessage;
import greencity.dto.PageableAdvancedDto;
import greencity.dto.event.AddEventDtoRequest;
import greencity.dto.event.EventDto;
import greencity.dto.tag.TagVO;
import greencity.dto.user.UserVO;
import greencity.entity.DateLocation;
import greencity.entity.Event;
import greencity.entity.Tag;
import greencity.entity.User;
import greencity.enums.Role;
import greencity.enums.TagType;
import greencity.exception.exceptions.NotFoundException;
import greencity.exception.exceptions.NotSavedException;
import greencity.exception.exceptions.UserHasNoPermissionToAccessException;
import greencity.repository.EventRepo;
import greencity.repository.TagTranslationRepo;
import greencity.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepo eventRepo;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final TagsService tagService;
    private final TagTranslationRepo tagTranslationRepo;
    private final FileService fileService;

    @Override
    public EventDto save(AddEventDtoRequest addEventDtoRequest, MultipartFile[] images, Long organizerId) {
        Event savedEvent = genericSave(addEventDtoRequest, images, organizerId);
        return modelMapper.map(savedEvent, EventDto.class);
    }

    private Event genericSave(AddEventDtoRequest addEventDtoRequest, MultipartFile[] images, Long organizerId) {
        User organizer = userRepo.findById(organizerId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND_BY_ID + organizerId));

        Event eventToSave = modelMapper.map(addEventDtoRequest, Event.class);
        eventToSave.setOrganizer(organizer);

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
                .map(eventDateLocationDto -> modelMapper.map(eventDateLocationDto, DateLocation.class)
                        .setEvent(eventToSave))
                .collect(Collectors.toList()));

        return eventRepo.save(eventToSave);
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

    @Override
    public PageableAdvancedDto<EventDto> findAll(Pageable page) {
        return null;
    }

    @Override
    public EventDto update(EventDto eventDto) {
        return null;
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

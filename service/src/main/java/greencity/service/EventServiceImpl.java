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
import greencity.enums.TagType;
import greencity.exception.exceptions.NotFoundException;
import greencity.exception.exceptions.NotSavedException;
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

        List<TagVO> tagVOS = tagService.findTagsByNamesAndType(
                addEventDtoRequest.getTags(), TagType.EVENT);
        List<Tag> tags = modelMapper.map(tagVOS,
                new TypeToken<List<Tag>>() {
                }.getType());
        tags = tags.stream()
                .map(tag ->
                        tag.setTagTranslations(tagTranslationRepo.findAllByTagId(tag.getId()))).collect(Collectors.toList());
        eventToSave.setTags(tags);
        System.out.println(eventToSave.getTags().get(0).getTagTranslations());
        eventToSave.setDateLocations(addEventDtoRequest.getDatesLocations().stream()
                .map(eventDateLocationDto -> modelMapper.map(eventDateLocationDto, DateLocation.class)
                        .setEvent(eventToSave))
                .collect(Collectors.toList()));

        return eventRepo.save(eventToSave);
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

package greencity.service;

import greencity.ModelUtils;
import greencity.TestConst;
import greencity.client.RestClient;
import greencity.dto.PageableAdvancedDto;
import greencity.dto.event.AddEventDtoRequest;
import greencity.dto.event.EventAttenderDto;
import greencity.dto.event.EventAuthorDto;
import greencity.dto.event.EventDto;
import greencity.dto.tag.TagUaEnDto;
import greencity.dto.tag.TagVO;
import greencity.dto.user.AttendersEmailsDto;
import greencity.dto.user.UserVO;
import greencity.entity.*;
import greencity.entity.localization.TagTranslation;
import greencity.enums.TagType;
import greencity.exception.exceptions.NotFoundException;
import greencity.exception.exceptions.NotSavedException;
import greencity.exception.exceptions.UnsupportedSortException;
import greencity.repository.EventRepo;
import greencity.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.time.ZonedDateTime;
import java.util.*;

import static greencity.ModelUtils.*;
import static greencity.constant.AppConstant.AUTHORIZATION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class EventServiceImplTest {
    @Mock
    FileService fileService;
    @Mock
    TagsService tagsService;
    @Mock
    UserService userService;
    @Mock
    ModelMapper modelMapper;
    @Mock
    RestClient restClient;
    @Mock
    HttpServletRequest httpServletRequest;
    @Mock
    private EventRepo eventRepo;
    @Mock
    private UserRepo userRepo;
    @InjectMocks
    private EventServiceImpl eventService;
    private Event event = getEvent();
    private Event notValidEvent = getNotValidEvent();

    @Test
    void save() throws MalformedURLException {
        MultipartFile[] images = new MultipartFile[] {getImage()};
        AddEventDtoRequest addEventDtoRequest = getAddEventDtoRequest();
        List<TagVO> tagVOS = Collections.singletonList(ModelUtils.getEventTagVO());
        List<Tag> tags = Collections.singletonList(getEventTag());

        when(modelMapper.map(addEventDtoRequest, Event.class)).thenReturn(event);
        when(modelMapper.map(event, EventDto.class)).thenReturn(getEventDto());
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(getUser()));
        when(eventRepo.save(event)).thenReturn(event);
        when(tagsService.findTagsWithAllTranslationsByNamesAndType(
            addEventDtoRequest.getTags(), TagType.EVENT)).thenReturn(tagVOS);
        when(modelMapper.map(tagVOS, new TypeToken<List<Tag>>() {
        }.getType())).thenReturn(tags);
        when(modelMapper.map(getEventDateLocationDto(), DateLocation.class)).thenReturn(getDateLocation());
        when(httpServletRequest.getHeader(AUTHORIZATION)).thenReturn("Bearer token");
        when(fileService.upload(any())).thenReturn(ModelUtils.getUrl().toString());
        doNothing().when(restClient).addEvent(any(), anyString());

        EventDto res = eventService.save(addEventDtoRequest, images, 1L);

        assertEquals(res, getEventDto());
    }

    @Test()
    void saveThrowsNotFoundException() {
        MultipartFile[] images = new MultipartFile[] {getFile()};

        when(userRepo.findById(4L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> eventService.save(getAddEventDtoRequest(), images, 4L));
    }

    @Test()
    void saveThrowsNotSavedException() throws MalformedURLException {
        MultipartFile[] images = new MultipartFile[] {getFile()};
        AddEventDtoRequest addEventDtoRequest = getAddEventDtoRequest();
        addEventDtoRequest.setDatesLocations(List.of(getInvalidEventDateLocationDto()));
        List<TagVO> tagVOS = Collections.singletonList(ModelUtils.getEventTagVO());
        List<Tag> tags = Collections.singletonList(getEventTag());

        when(modelMapper.map(addEventDtoRequest, Event.class)).thenReturn(getNotValidEvent());
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(getUser()));
        when(tagsService.findTagsWithAllTranslationsByNamesAndType(
            addEventDtoRequest.getTags(), TagType.EVENT)).thenReturn(tagVOS);
        when(modelMapper.map(tagVOS, new TypeToken<List<Tag>>() {
        }.getType())).thenReturn(tags);
        when(fileService.upload(any())).thenReturn(ModelUtils.getUrl().toString());

        assertThrows(NotSavedException.class, () -> eventService.save(addEventDtoRequest, images, 1L));
    }

    @Test
    void delete() {
        when(eventRepo.findById(anyLong())).thenReturn(Optional.of(event));
        when(eventService.findById(anyLong())).thenReturn(getEventDto());

        eventService.delete(event.getId(), getUserVO());

        verify(eventRepo, times(1)).deleteById(anyLong());
    }

    @Test
    void findById() {
        EventDto expected = modelMapper.map(event, EventDto.class);
        when(eventRepo.findById(anyLong())).thenReturn(Optional.ofNullable(event));

        EventDto actual = eventService.findById(event.getId());

        verify(eventRepo, times(1)).findById(anyLong());
        assertEquals(expected, actual);
    }

    @Test
    void findAllEvents() {
        ZonedDateTime now = ZonedDateTime.now();
        Tag tag = new Tag();
        tag.setTagTranslations(
            List.of(TagTranslation.builder().name("Social").language(Language.builder().code("en").build()).build(),
                TagTranslation.builder().name("Соціальний").language(Language.builder().code("ua").build()).build()));

        TagUaEnDto tagUaEnDto = TagUaEnDto.builder().id(1L).nameUa("Соціальний").nameEn("Social").build();

        List<Event> events = Collections.singletonList(ModelUtils.getEvent());
        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<Event> translationPage = new PageImpl<>(events,
            pageRequest, events.size());

        EventAuthorDto authorDto = EventAuthorDto.builder()
            .id(1L)
            .name(TestConst.NAME)
            .build();

        List<EventDto> dtoList = Collections.singletonList(
            EventDto.builder()
                .creationDate(now)
                .id(1L)
                .title("Title")
                .description("description description description")
                .organizer(authorDto)
                .tags(Set.of(tagUaEnDto))
                .build());

        PageableAdvancedDto<EventDto> pageableDto = new PageableAdvancedDto<>(dtoList, dtoList.size(), 0, 1,
            0, false, false, true, true);

        when(eventRepo.findAllByOrderByCreationDateDesc(pageRequest)).thenReturn(translationPage);
        when(modelMapper.map(events.get(0), EventDto.class)).thenReturn(dtoList.get(0));

        PageableAdvancedDto<EventDto> actual = eventService.findAll(pageRequest);

        assertEquals(pageableDto, actual);
    }

    @Test
    public void testRateEvent() {
        Event event = ModelUtils.getEvents();
        User user = ModelUtils.getUser();
        event.setAttenders(List.of(user));
        when(eventRepo.findById(any())).thenReturn(Optional.of(event));
        when(modelMapper.map(restClient.findByEmail(user.getEmail()), User.class)).thenReturn(user);
        doNothing().when(userService).updateEventOrganizerRating(event.getOrganizer().getId(), 2.0);
        List<Event> events = List.of(event, ModelUtils.getEventWithGrades());
        when(eventRepo.getAllByOrganizer(event.getOrganizer())).thenReturn(events);
        eventService.rateEvent(event.getId(), user.getEmail(), 2);
        verify(eventRepo).save(event);
    }

    @Test
    void findAllByUserPage() {
        List<Event> events = Collections.singletonList(ModelUtils.getEvent());
        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<Event> translationPage = new PageImpl<>(events,
            pageRequest, events.size());

        List<EventDto> dtoList = Collections.singletonList(
            ModelUtils.getEventDto());
        PageableAdvancedDto<EventDto> pageableDto = new PageableAdvancedDto<>(dtoList, dtoList.size(), 0, 1,
            0, false, false, true, true);

        UserVO userVO = UserVO.builder().id(1L).build();
        User user = User.builder().id(1L).build();
        when(modelMapper.map(userVO, User.class)).thenReturn(user);
        when(eventRepo.findAllByOrganizerOrderByCreationDateDesc(user, pageRequest)).thenReturn(translationPage);
        when(userRepo.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(modelMapper.map(events.get(0), EventDto.class)).thenReturn(dtoList.get(0));

        PageableAdvancedDto<EventDto> actual = eventService.findAllByUser(userVO, pageRequest);

        assertEquals(pageableDto, actual);
    }

    @Test
    void findAllByUserPageInvalidSorted() {
        PageRequest pageRequest = PageRequest.of(0, 1, Sort.by("id"));

        when(userRepo.findById(1L)).thenReturn(Optional.ofNullable(ModelUtils.getUser()));
        UserVO userVO = UserVO.builder().id(1L).build();

        assertThrows(UnsupportedSortException.class, () -> eventService.findAllByUser(userVO, pageRequest));
    }


    @Test
    void update() throws MalformedURLException {
        MultipartFile[] images = new MultipartFile[] {getImage()};
        AddEventDtoRequest addEventDtoRequest = getAddEventDtoRequest();
        List<TagVO> tagVOS = Collections.singletonList(ModelUtils.getEventTagVO());
        List<Tag> tags = Collections.singletonList(getEventTag());
        List<User> attenders = new ArrayList<>();
        User attender1 = ModelUtils.getUser();
        attenders.add(attender1);
        event.setAttenders(attenders);
        when(eventRepo.findById(anyLong())).thenReturn(Optional.ofNullable(event));
        when(modelMapper.map(addEventDtoRequest, Event.class)).thenReturn(event);
        when(modelMapper.map(event, EventDto.class)).thenReturn(getEventDto());
        when(userRepo.findById(anyLong())).thenReturn(Optional.of(getUser()));
        when(eventRepo.save(event)).thenReturn(event);
        when(tagsService.findTagsWithAllTranslationsByNamesAndType(
            addEventDtoRequest.getTags(), TagType.EVENT)).thenReturn(tagVOS);
        when(modelMapper.map(tagVOS, new TypeToken<List<Tag>>() {
        }.getType())).thenReturn(tags);
        when(modelMapper.map(getEventDateLocationDto(), DateLocation.class)).thenReturn(getDateLocation());
        when(httpServletRequest.getHeader(AUTHORIZATION)).thenReturn("Bearer token");
        when(fileService.upload(any())).thenReturn(ModelUtils.getUrl().toString());
        List<AttendersEmailsDto> emailsDtos = new ArrayList<>();
        EventDto res = eventService.update(addEventDtoRequest, images, 1L);
        res.setAttendersEmailsDtos(emailsDtos);
        assertEquals(getEventDto(), res);
    }

    @Test
    void testAddAttenderToEvent() {
        Long eventId = 1L;
        Long attenderId = 2L;
        List<User> attenders = new ArrayList<>();
        List<User> friends = new ArrayList<>();
        event.setEventClosed(false);
        event.setAttenders(attenders);
        User attender = ModelUtils.getUser();
        when(userRepo.existsById(attenderId)).thenReturn(true);
        when(eventRepo.existsById(eventId)).thenReturn(true);
        when(userRepo.getAllUserFriends(any())).thenReturn(friends);
        when(eventRepo.getOne(eventId)).thenReturn(event);
        when(eventRepo.findById(eventId)).thenReturn(Optional.of(event));
        when(userRepo.getOne(attenderId)).thenReturn(attender);
        doNothing().when(eventRepo).addAttender(attenderId, eventId);
        eventService.addAttenderToEvent(eventId, attenderId);

        verify(eventRepo, times(2)).findById(eventId);
        verify(eventRepo, times(2)).getOne(eventId);
        verify(eventRepo, times(1)).addAttender(attenderId, eventId);
    }

    @Test
    void testRemoveAttenderFromEvent() {
        Long eventId = 1L;
        Long attenderId = 2L;

        User organizer = ModelUtils.getUser();
        List<User> attenders = new ArrayList<>();
        User attender = new User();
        attender.setId(2L);
        attender.setName("Marko");
        attenders.add(attender);

        event.setOrganizer(organizer);
        event.setAttenders(attenders);

        when(eventRepo.getOne(eventId)).thenReturn(event);
        when(eventRepo.findById(eventId)).thenReturn(Optional.ofNullable(event));
        when(userRepo.getOne(attenderId)).thenReturn(attender);
        eventService.removeAttenderFromEvent(eventId, attenderId);
        verify(eventRepo, times(2)).findById(eventId);
        verify(eventRepo, times(1)).getOne(eventId);
        verify(userRepo, times(1)).getOne(attenderId);
    }

    @Test
    void testGetAllSubscribers() {

        List<User> attenders = new ArrayList<>();
        User user1 = ModelUtils.getUser();
        User user2 = new User();
        user2.setName("Marko");
        attenders.add(user1);
        attenders.add(user2);
        event.setAttenders(attenders);
        when(eventRepo.findById(event.getId())).thenReturn(Optional.of(event));

        List<EventAttenderDto> eventAttenderDtos = eventService.getAllSubscribers(event.getId());

        assertFalse(eventAttenderDtos.isEmpty());
        assertEquals(2, eventAttenderDtos.size());
        verify(eventRepo, times(1)).findById(event.getId());
    }

    @Test
    void findAllByAttenderId() {
        List<Event> events = Collections.singletonList(event);
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<Event> page = new PageImpl<>(events,
            pageRequest, events.size());

        List<EventDto> eventDtoList = Collections.singletonList(getEventDto());
        PageableAdvancedDto<EventDto> expected =
            new PageableAdvancedDto<>(eventDtoList, eventDtoList.size(), 0, 1, 0, false, false, true, true);

        UserVO userVO = getUserVO();
        User user = getUser();

        when(modelMapper.map(userVO, User.class)).thenReturn(user);
        when(eventRepo.findAllByAttenderIdOrderByCreationDateDesc(1L, pageRequest)).thenReturn(page);
        when(userRepo.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(modelMapper.map(events.get(0), EventDto.class)).thenReturn(eventDtoList.get(0));

        PageableAdvancedDto<EventDto> actual = eventService.findAllByAttenderId(1L, pageRequest);

        assertEquals(expected, actual);
    }

    @Test
    void findAllRelatedToUser() {
        List<Event> events = Collections.singletonList(event);
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<Event> page = new PageImpl<>(events,
            pageRequest, events.size());

        List<EventDto> eventDtoList = Collections.singletonList(getEventDto());
        PageableAdvancedDto<EventDto> expected =
            new PageableAdvancedDto<>(eventDtoList, eventDtoList.size(), 0, 1, 0, false, false, true, true);

        UserVO userVO = getUserVO();
        User user = getUser();

        when(modelMapper.map(userVO, User.class)).thenReturn(user);
        when(eventRepo.findAllRelatedToUserOrderByCreationDateDesc(1L, pageRequest)).thenReturn(page);
        when(userRepo.findById(1L)).thenReturn(Optional.ofNullable(user));
        when(modelMapper.map(events.get(0), EventDto.class)).thenReturn(eventDtoList.get(0));

        PageableAdvancedDto<EventDto> actual = eventService.findAllRelatedToUser(1L, pageRequest);

        assertEquals(expected, actual);
    }

    @Test
    void addToFavorites() {
        when(eventRepo.findById(anyLong())).thenReturn(Optional.ofNullable(event));
        doNothing().when(eventRepo).addToFavorites(anyLong());

        eventService.addToFavorites(event.getId());

        verify(eventRepo).findById(event.getId());
        verify(eventRepo).addToFavorites(event.getId());
    }

    @Test
    void removeFromFavourites() {
        when(eventRepo.findById(anyLong())).thenReturn(Optional.ofNullable(event));
        doNothing().when(eventRepo).removeFromFavorites(anyLong());

        eventService.removeFromFavorites(event.getId());

        verify(eventRepo).findById(event.getId());
        verify(eventRepo).removeFromFavorites(event.getId());
    }
}
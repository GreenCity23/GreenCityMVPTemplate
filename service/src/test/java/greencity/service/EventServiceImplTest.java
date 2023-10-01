package greencity.service;

import greencity.ModelUtils;
import greencity.client.RestClient;
import greencity.dto.event.AddEventDtoRequest;
import greencity.dto.event.EventDto;
import greencity.dto.tag.TagVO;
import greencity.entity.DateLocation;
import greencity.entity.Event;
import greencity.entity.Tag;
import greencity.enums.TagType;
import greencity.repository.EventRepo;
import greencity.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static greencity.ModelUtils.*;
import static greencity.constant.AppConstant.AUTHORIZATION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class EventServiceImplTest {
    @Mock
    FileService fileService;
    @Mock
    TagsService tagsService;
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

    @Test
    void save() throws MalformedURLException {
        MultipartFile[] images = new MultipartFile[]{getFile()};
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
}

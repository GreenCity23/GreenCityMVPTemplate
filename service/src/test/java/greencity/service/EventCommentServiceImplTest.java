package greencity.service;

import greencity.ModelUtils;
import greencity.dto.eventcomments.AddEventCommentDtoRequest;
import greencity.dto.eventcomments.EventCommentDto;
import greencity.dto.user.UserVO;
import greencity.entity.Event;
import greencity.entity.EventComment;
import greencity.entity.User;
import greencity.exception.exceptions.BadRequestException;
import greencity.exception.exceptions.NotFoundException;
import greencity.repository.EventCommentRepo;
import greencity.repository.EventRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static greencity.ModelUtils.getUser;
import static greencity.ModelUtils.getUserVO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventCommentServiceImplTest {

    @InjectMocks
    private EventCommentServiceImpl eventCommentService;
    @Mock
    private EventRepo eventRepo;
    @Mock
    private EventCommentRepo eventCommentRepo;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private HttpServletRequest httpServletRequest;

    @Test
    void addCommentTest() {
        UserVO userVO = getUserVO();
        User user = getUser();
        AddEventCommentDtoRequest addEventCommentDtoRequest = ModelUtils.getAddEventCommentDtoRequest();

        when(modelMapper.map(userVO, User.class)).thenReturn(user);
        when(eventRepo.findById(1L)).thenReturn(Optional.of(ModelUtils.getEvent()));
        when(modelMapper.map(addEventCommentDtoRequest, EventComment.class)).thenReturn(ModelUtils.getEventComment());
        when(eventCommentRepo.save(any())).thenReturn(ModelUtils.getEventComment());

        eventCommentService.save(addEventCommentDtoRequest,1L, userVO);
        verify(eventCommentRepo).save(any(EventComment.class));
    }

    @Test
    void addReplyCommentTest() {
        UserVO userVO = getUserVO();
        User user = getUser();
        AddEventCommentDtoRequest addEventCommentDtoRequest = ModelUtils.getAddEventCommentDtoRequest();

        when(modelMapper.map(userVO, User.class)).thenReturn(user);
        when(eventRepo.findById(1L)).thenReturn(Optional.of(ModelUtils.getEvent()));
        when(modelMapper.map(addEventCommentDtoRequest, EventComment.class)).thenReturn(ModelUtils.getEventComment());
        when(eventCommentRepo.save(any())).thenReturn(ModelUtils.getEventComment());

        eventCommentService.save(addEventCommentDtoRequest,1L, userVO);
        verify(eventCommentRepo).save(any(EventComment.class));
    }

    @Test
    void likeCommentByIdTest() {
        UserVO userVO = getUserVO();

        when(eventCommentRepo.findById(1L)).thenReturn(Optional.of(ModelUtils.getEventComment()));
        when(httpServletRequest.getHeader(any())).thenReturn("token");

        assertDoesNotThrow(() -> eventCommentService.likeCommentById(1L, userVO));
    }

    @Test
    void updateCommentTest() {
        UserVO userVO = getUserVO();
        EventComment eventComment = ModelUtils.getEventComment();
        eventComment.setText("new text");

        when(eventCommentRepo.findById(1L)).thenReturn(Optional.of(ModelUtils.getEventComment()));

        eventCommentService.update(1L,"new text", userVO);

        verify(eventCommentRepo).save(any(EventComment.class));
    }

    @Test
    void updateCommentThrowExceptionWhenNotEventFoundTest() {
        UserVO userVO = getUserVO();
        EventComment eventComment = ModelUtils.getEventComment();
        eventComment.setText("new text");

        when(eventCommentRepo.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> eventCommentService.update(2L,"new text", userVO));
    }

    @Test
    void userDeleteOwnCommentByIdTest() {
        UserVO userVO = getUserVO();
        when(eventCommentRepo.findById(1L)).thenReturn(Optional.of(ModelUtils.getEventComment()));

        eventCommentService.deleteById(1L, userVO);
        verify(eventCommentRepo).delete(any(EventComment.class));
    }

    @Test
    void userDeleteAnotherCommentTest() {
        UserVO userVO = getUserVO();
        EventComment eventComment = ModelUtils.getEventComment();
        eventComment.getUser().setId(2L);
        when(eventCommentRepo.findById(1L)).thenReturn(Optional.of(eventComment));

        assertThrows(BadRequestException.class, () -> eventCommentService.deleteById(1L, userVO));
    }

    @Test
    void findEventCommentByIdTest() {
        EventComment eventComment = ModelUtils.getEventComment();
        when(eventCommentRepo.findById(1L)).thenReturn(Optional.ofNullable(eventComment));
        when(modelMapper.map(eventComment, EventCommentDto.class)).thenReturn(ModelUtils.getEventCommentDto());

        EventCommentDto actual = eventCommentService.findEventCommentById(1L);
        assertEquals(eventComment.getText(), actual.getText());
        assertEquals(eventComment.getUser().getId(), actual.getAuthor().getId());
        assertEquals(eventComment.getUsersLiked().size(), actual.getLikes());
    }

    @Test
    void getEventCommentsAmountTest() {
        Event event = ModelUtils.getEvent();
        EventComment eventComment = ModelUtils.getEventComment();
        event.getEventComments().add(eventComment);
        when(eventRepo.findById(1L)).thenReturn(Optional.of(event));

        int actual = eventCommentService.getEventCommentsAmount(1L);

        assertEquals(1, actual);
    }
}
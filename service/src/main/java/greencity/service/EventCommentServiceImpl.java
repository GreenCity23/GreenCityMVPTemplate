package greencity.service;

import greencity.annotations.RatingCalculationEnum;
import greencity.client.RestClient;
import greencity.constant.ErrorMessage;
import greencity.dto.PageableDto;
import greencity.dto.eventcomments.EventCommentForSendDto;
import greencity.dto.event.EventDto;
import greencity.dto.eventcomments.AddEventCommentDtoRequest;
import greencity.dto.eventcomments.AddEventCommentDtoResponse;
import greencity.dto.eventcomments.AmountCommentLikesDto;
import greencity.dto.eventcomments.EventCommentDto;
import greencity.dto.user.UserVO;
import greencity.entity.Event;
import greencity.entity.EventComment;
import greencity.entity.User;
import greencity.exception.exceptions.BadRequestException;
import greencity.exception.exceptions.NotFoundException;
import greencity.rating.RatingCalculation;
import greencity.repository.EventCommentRepo;
import greencity.repository.EventRepo;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static greencity.constant.AppConstant.AUTHORIZATION;

@Service
@AllArgsConstructor
public class EventCommentServiceImpl implements EventCommentService{
    private final EventRepo eventRepo;
    private final EventCommentRepo eventCommentRepo;
    private final ModelMapper modelMapper;
    private final RatingCalculation ratingCalculation;
    private final HttpServletRequest httpServletRequest;
    private final RestClient restClient;

    /**
     * {@inheritDoc}
     */
    @Override
    public AddEventCommentDtoResponse save(AddEventCommentDtoRequest addEventCommentDtoRequest, Long eventId, UserVO userVO) {
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUND_BY_ID + eventId));
        EventComment eventComment = modelMapper.map(addEventCommentDtoRequest, EventComment.class);
        eventComment.setEvent(event);
        eventComment.setUser(modelMapper.map(userVO, User.class));
        eventComment.setCreatedDate(LocalDateTime.now());
        eventComment.setModifiedDate(LocalDateTime.now());
        if (addEventCommentDtoRequest.getParentCommentId() != 0) {
            EventComment parentComment = eventCommentRepo.findById(addEventCommentDtoRequest.getParentCommentId())
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_COMMENT_NOT_FOUND_BY_ID +
                                                             addEventCommentDtoRequest.getParentCommentId()));
            if (parentComment.getParentComment() == null) {
                eventComment.setParentComment(parentComment);
            } else {
                throw new BadRequestException(ErrorMessage.CANNOT_REPLY_THE_REPLY);
            }
        }
        String accessToken = httpServletRequest.getHeader(AUTHORIZATION);
        CompletableFuture.runAsync(
                () -> ratingCalculation.ratingCalculation(RatingCalculationEnum.ADD_COMMENT, userVO, accessToken));
        if(event.getOrganizer().getId() != eventComment.getUser().getId()) {
            sendEmailNotificationToOrganizer(modelMapper.map(eventComment,EventCommentDto.class),
                    modelMapper.map(event, EventDto.class));
        }
        return modelMapper.map(eventCommentRepo.save(eventComment), AddEventCommentDtoResponse.class);
    }

    /**
     * {@inheritDoc}
     */
    private void sendEmailNotificationToOrganizer(EventCommentDto eventCommentDto, EventDto eventDto) {
        EventCommentForSendDto eventCommentForSendDto = EventCommentForSendDto.builder()
                .eventName(eventDto.getTitle())
                .eventAuthorDto(eventDto.getOrganizer())
                .eventCommentCreationDate(eventCommentDto.getCreatedDate())
                .eventCommentText(eventCommentDto.getText())
                .eventCommentAuthorDto(eventCommentDto.getAuthor())
                .build();
        restClient.sendEmailAfterEmailWasCommented(eventCommentForSendDto,
                httpServletRequest.getHeader(AUTHORIZATION).substring(7));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Long id, String commentText, UserVO userVO) {
        EventComment comment = eventCommentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_COMMENT_NOT_FOUND_BY_ID + id));
        if (!userVO.getId().equals(comment.getUser().getId())) {
            throw new BadRequestException(ErrorMessage.NOT_A_CURRENT_USER);
        }
        comment.setText(commentText);
        comment.setModifiedDate(LocalDateTime.now());
        eventCommentRepo.save(comment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void likeComment(Long id, UserVO userVO) {
        EventComment comment = eventCommentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_COMMENT_NOT_FOUND_BY_ID + id));
        if (comment.getUsersLiked().stream()
                .anyMatch(user -> user.getId().equals(userVO.getId()))) {
            unlikeComment(userVO, comment);
        } else {
            likeComment(userVO, comment);
        }
        eventCommentRepo.save(comment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(Long id, UserVO userVO) {
        EventComment eventComment = eventCommentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_COMMENT_NOT_FOUND_BY_ID + id));
        if (!userVO.getId().equals(eventComment.getUser().getId())) {
            throw new BadRequestException(ErrorMessage.NOT_A_CURRENT_USER);
        }
        eventCommentRepo.delete(eventComment);
    }

    /**
     * {@inheritDoc}
     */
    private void likeComment(UserVO userVO, EventComment eventComment) {
        User user = modelMapper.map(userVO, User.class);
        eventComment.getUsersLiked().add(user);
        String accessToken = httpServletRequest.getHeader(AUTHORIZATION);
        CompletableFuture
                .runAsync(() -> ratingCalculation.ratingCalculation(RatingCalculationEnum.LIKE_COMMENT, userVO, accessToken));
    }

    /**
     * {@inheritDoc}
     */
    private void unlikeComment(UserVO user, EventComment eventComment) {
        String accessToken = httpServletRequest.getHeader(AUTHORIZATION);
        eventComment.getUsersLiked().removeIf(u -> u.getId().equals(user.getId()));
        CompletableFuture
            .runAsync(() -> ratingCalculation.ratingCalculation(RatingCalculationEnum.LIKE_COMMENT, user, accessToken));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EventCommentDto findEventCommentById(Long id) {
        EventComment eventComment = eventCommentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_COMMENT_NOT_FOUND_BY_ID + id));
        return modelMapper.map(eventComment, EventCommentDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getEventCommentsAmount(Long eventId) {
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUND_BY_ID + eventId));
        return event.getEventComments().size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getCountOfCommentReplies(Long id) {
        if(eventCommentRepo.findById(id).isEmpty()) {
            throw new NotFoundException(ErrorMessage.EVENT_NOT_FOUND_BY_ID + id);
        }
        return eventCommentRepo.countByParentCommentId(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AmountCommentLikesDto getCountOfCommentLikes(Long id, UserVO userVO) {
        EventComment eventComment = eventCommentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.EVENT_NOT_FOUND_BY_ID + id));
        return modelMapper.map(eventComment, AmountCommentLikesDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageableDto<EventCommentDto> getAllActiveReplies(Pageable pageable, Long parentCommentId, UserVO userVO) {
        Page<EventComment> pages = eventCommentRepo.findAllByParentCommentIdAndDeletedFalseOrderByCreatedDateDesc(pageable, parentCommentId);

        UserVO user = userVO == null ? UserVO.builder().build() : userVO;
        List<EventCommentDto> eventCommentDTOs = pages.stream()
                .map(comment -> {
                    comment.setCurrentUserLiked(comment.getUsersLiked().stream()
                            .anyMatch(u -> u.getId().equals(user.getId())));
                    return comment;
                })
                .map(eventComment -> modelMapper.map(eventComment, EventCommentDto.class))
                .collect(Collectors.toList());

        return new PageableDto<>(
                eventCommentDTOs,
                pages.getTotalElements(),
                pages.getPageable().getPageNumber(),
                pages.getTotalPages());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageableDto<EventCommentDto> getAllActiveComments(Pageable pageable, UserVO userVO, Long eventId) {
        Page<EventComment> pages =
                eventCommentRepo
                        .findAllByParentCommentIsNullAndDeletedFalseAndEventIdOrderByCreatedDateDesc(pageable, eventId);
        UserVO user = userVO == null ? UserVO.builder().build() : userVO;
        List<EventCommentDto> eventCommentsDTOs = pages
                .stream()
                .map(comment -> {
                    comment.setCurrentUserLiked(comment.getUsersLiked().stream()
                            .anyMatch(u -> u.getId().equals(user.getId())));
                    return comment;
                })
                .map(eventComment -> modelMapper.map(eventComment, EventCommentDto.class))
                .map(comment -> {
                    comment.setReplies(eventCommentRepo.countByParentCommentId(comment.getId()));
                    return comment;
                })
                .collect(Collectors.toList());

        return new PageableDto<>(
                eventCommentsDTOs,
                pages.getTotalElements(),
                pages.getPageable().getPageNumber(),
                pages.getTotalPages());
    }
}

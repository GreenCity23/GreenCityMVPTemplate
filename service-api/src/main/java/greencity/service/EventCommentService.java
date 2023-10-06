package greencity.service;

import greencity.dto.PageableDto;
import greencity.dto.eventcomments.*;
import greencity.dto.user.UserVO;
import org.springframework.data.domain.Pageable;

public interface EventCommentService {

    /**
     * Method to save {@link EventCommentVO}.
     *
     * @param eventId                   id of event to which we save
     *                                    comment.
     * @param addEventCommentDtoRequest dto with {@link EventCommentVO} text,
     *                                    parentCommentId.
     * @param user                      {@link UserVO} that saves the comment.
     * @return {@link AddEventCommentDtoResponse} instance.
     */
    AddEventCommentDtoResponse save(AddEventCommentDtoRequest addEventCommentDtoRequest, Long eventId, UserVO user);

    /**
     * Method to change the existing {@link EventCommentVO}.
     *
     * @param commentText new text of {@link EventCommentVO}.
     * @param id   to specify {@link EventCommentVO} that user wants to change.
     * @param user current {@link UserVO} that wants to change.
     */
    void update(Long id, String commentText, UserVO user);

    /**
     * Method to like or dislike {@link EventCommentVO} specified
     * by id.
     *
     * @param id     of {@link EventCommentVO} to like/dislike.
     * @param userVO current User that wants to like/dislike.
     */
    void likeComment(Long id, UserVO userVO);

    void deleteById(Long id);

    EventCommentDto findEventCommentById(Long id);

    Integer getEventCommentsAmount(Long id);

    Integer getCountOfCommentReplies(Long id);

    AmountCommentLikesDto getCountOfCommentLikes(Long id, UserVO userVO);

    PageableDto<EventCommentDto> getAllActiveReplies(Pageable pageable, Long parentCommentId, UserVO user);

    PageableDto<EventCommentDto> getAllActiveComments(Pageable pageable, UserVO user, Long eventId);
}

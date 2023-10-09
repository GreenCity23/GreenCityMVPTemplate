package greencity.service;

import greencity.dto.PageableDto;
import greencity.dto.eventcomments.*;
import greencity.dto.user.UserVO;
import org.springframework.data.domain.Pageable;

public interface EventCommentService {

    /**
     * Method to save {@link EventCommentVO}.
     *
     * @param eventId                   id of event to which we save comment.
     * @param addEventCommentDtoRequest dto with {@link EventCommentVO} text,
     *                                  parentCommentId.
     * @param user                      {@link UserVO} that saves the comment.
     * @return {@link AddEventCommentDtoResponse} instance.
     */
    AddEventCommentDtoResponse save(AddEventCommentDtoRequest addEventCommentDtoRequest, Long eventId, UserVO user);

    /**
     * Method to change the existing {@link EventCommentVO}.
     *
     * @param commentText new text of {@link EventCommentVO}.
     * @param id          to specify {@link EventCommentVO} that user wants to
     *                    change.
     * @param user        current {@link UserVO} that wants to change.
     */
    void update(Long id, String commentText, UserVO user);

    /**
     * Method to like or dislike {@link EventCommentVO} specified by id.
     *
     * @param id     of {@link EventCommentVO} to like/dislike.
     * @param userVO current User that wants to like/dislike.
     */
    void likeCommentById(Long id, UserVO userVO);

    /**
     * Method to delete {@link EventCommentVO} by id.
     *
     * @param id     of {@link EventCommentVO}.
     * @param userVO current User that wants to delete by id.
     */
    void deleteById(Long id, UserVO userVO);

    /**
     * Method to find {@link EventCommentVO} by id.
     *
     * @param id of {@link EventCommentVO}.
     */
    EventCommentDto findEventCommentById(Long id);

    /**
     * Method to get comments amount by event id.
     *
     * @param id of Event.
     */
    Integer getEventCommentsAmount(Long id);

    /**
     * Method to get comment replies amount by comment id.
     *
     * @param id of Event Comment.
     */
    Integer getCountOfCommentReplies(Long id);

    /**
     * Method to get amount of comment likes.
     *
     * @param id     of Event Comment.
     * @param userVO current User.
     */
    AmountCommentLikesDto getCountOfCommentLikes(Long id, UserVO userVO);

    /**
     * Method to get all active comment replies.
     *
     * @param parentCommentId id parent event comment.
     * @param user            current User.
     */
    PageableDto<EventCommentDto> getAllActiveReplies(Pageable pageable, Long parentCommentId, UserVO user);

    /**
     * Method to get all active comments.
     *
     * @param eventId id event.
     * @param user    current User.
     */
    PageableDto<EventCommentDto> getAllActiveComments(Pageable pageable, UserVO user, Long eventId);
}

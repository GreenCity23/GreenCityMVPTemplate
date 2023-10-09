package greencity.controller;

import greencity.annotations.ApiPageable;
import greencity.annotations.CurrentUser;
import greencity.constant.HttpStatuses;
import greencity.dto.PageableDto;
import greencity.dto.eventcomments.AddEventCommentDtoRequest;
import greencity.dto.eventcomments.AddEventCommentDtoResponse;
import greencity.dto.eventcomments.AmountCommentLikesDto;
import greencity.dto.eventcomments.EventCommentDto;
import greencity.dto.user.UserVO;
import greencity.service.EventCommentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/events/comments")
public class EventCommentController {
    private final EventCommentService eventCommentService;

    /**
     * Method to count likes for comment.
     *
     * @param commentId comment id
     */
    @ApiOperation(value = "Count likes for comment.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
    })
    @GetMapping("/likes/count/{commentId}")
    public ResponseEntity<AmountCommentLikesDto> getEventCommentLikesAmount(@PathVariable Long commentId,
                                                                            @ApiIgnore @CurrentUser UserVO userVO) {
        return ResponseEntity.ok(eventCommentService.getCountOfCommentLikes(commentId, userVO));
    }

    /**
     * Method to mark event comment as deleted.
     *
     * @param eventCommentId comment id
     */
    @ApiOperation(value = "Mark comment as deleted.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
    })
    @DeleteMapping("/{eventCommentId}")
    public void deleteEventCommentById(@PathVariable Long eventCommentId, @ApiIgnore @CurrentUser UserVO userVO) {
        eventCommentService.deleteById(eventCommentId, userVO);
    }

    /**
     * Method to get all active comment replies
     * @param parentCommentId id of comment
     * @param user current user
     */
    @ApiOperation(value = "Get all active replies to event comment.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
    })
    @GetMapping("/replies/active/{parentCommentId}")
    @ApiPageable
    public ResponseEntity<PageableDto<EventCommentDto>> getAllActiveReplies(@ApiIgnore Pageable pageable,
            @PathVariable Long parentCommentId, @ApiIgnore @CurrentUser UserVO user) {
        return ResponseEntity.ok(eventCommentService.getAllActiveReplies(pageable, parentCommentId, user));
    }

    /**
     * Method to get all event active comments
     * @param eventId id of event
     * @param user current user
     */
    @ApiOperation(value = "Get all active comments.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
    })
    @GetMapping("/active")
    public ResponseEntity<PageableDto<EventCommentDto>> getAllActiveComments(@ApiIgnore Pageable pageable,
           Long eventId, @ApiIgnore @CurrentUser UserVO user) {
        return ResponseEntity.ok(eventCommentService.getAllActiveComments(pageable, user, eventId));
    }

    /**
     * Method to get amount of comment replies by comment id.
     * @param parentCommentId id of parent comment
     */
    @ApiOperation(value = "Count replies for comment.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
    })
    @GetMapping("/replies/count/{parentCommentId}")
    public ResponseEntity<Integer> getCountOfCommentReplies(@PathVariable Long parentCommentId) {
        return ResponseEntity.ok(eventCommentService.getCountOfCommentReplies(parentCommentId));
    }

    /**
     * Method to get amount of event comments by event id.
     * @param eventId id of event
     */
    @ApiOperation(value = "Count comments.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
    })

    @GetMapping("/count/{eventId}")
    public ResponseEntity<Integer> getEventCommentsAmount(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventCommentService.getEventCommentsAmount(eventId));
    }

    /**
     * Method to get event comment by id.
     * @param id id of comment
     */
    @ApiOperation(value = "Get event comment by id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
    })
    @GetMapping("/{id}")
    public ResponseEntity<EventCommentDto> getEventCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(eventCommentService.findEventCommentById(id));
    }

    /**
     * Method to put like on event comment.
     * @param id id of comment
     * @param userVO current user
     */
    @ApiOperation(value = "Like comment.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
    })
    @PostMapping("/like")
    public void likeComment(@RequestParam("id") Long id, @ApiIgnore @CurrentUser UserVO userVO) {
        eventCommentService.likeCommentById(id, userVO);
    }


    /**
     * Method to save event comment.
     * @param eventId id of commented event
     * @param userVO current user
     * @param addEventCommentDtoRequest comment text and parent comment id (optional)
     */
    @ApiOperation(value = "Add comment to event.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HttpStatuses.CREATED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
    })
    @PostMapping("/{eventId}")
    public ResponseEntity<AddEventCommentDtoResponse> addComment(@PathVariable Long eventId,
            @Valid @RequestBody AddEventCommentDtoRequest addEventCommentDtoRequest,
            @ApiIgnore @CurrentUser UserVO userVO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventCommentService.save(addEventCommentDtoRequest, eventId, userVO));
    }

    /**
     * Method to update event comment.
     * @param id id of comment
     * @param userVO current user
     * @param commentText edited comment text
     */
    @ApiOperation("Update comment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
    })
    @PatchMapping
    public void updateComment(@RequestParam("id") Long id,
        @RequestParam("commentText") String commentText,
        @ApiIgnore @CurrentUser UserVO userVO) {
        eventCommentService.update(id, commentText, userVO);
    }
}

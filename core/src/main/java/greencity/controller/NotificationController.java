package greencity.controller;

import greencity.annotations.CurrentUser;
import greencity.constant.HttpStatuses;
import greencity.dto.PageableDto;
import greencity.dto.notification.NotificationDto;
import greencity.dto.notification.NotificationDtoResponse;
import greencity.dto.user.UserVO;
import greencity.service.NotificationService;
import greencity.service.NotifiedUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/notifications")
@AllArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final NotifiedUserService notifiedUserService;

    /**
     * Method for creating new {@link NotificationDto}.
     *
     * @param notificationDto dto for {@link NotificationDto} entity.
     * @return dto {@link NotificationDto} instance.
     * @author Nazar Klimovych
     */
    @ApiOperation(value = "Add new notification.")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = HttpStatuses.CREATED, response = NotificationDto.class),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED)
    })
    @PostMapping
    public ResponseEntity<NotificationDto> saveNotification(@Valid @RequestBody NotificationDto notificationDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.save(notificationDto));
    }

    @ApiOperation(value = "Create EcoNewsComment notifications.")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = HttpStatuses.CREATED , response = NotificationDto.class,
                responseContainer = "List"),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @PostMapping("/econewscomment/{sourceId}")
    public ResponseEntity<List<NotificationDto>> save(@PathVariable Long sourceId){
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(notificationService.createEcoNewsCommentNotification(sourceId));
    }

    @ApiOperation(value = "Get all users notifications.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
    })
    @GetMapping("/user")
    public ResponseEntity<PageableDto<NotificationDtoResponse>> getNotifications(@ApiIgnore @CurrentUser UserVO user,
                                                                                 @ApiIgnore Pageable pageable) {
        PageableDto<NotificationDtoResponse>  notifications = notificationService.getNotificationsForUser(pageable, user.getId());
        return ResponseEntity.ok(notifications);
    }

    @ApiOperation(value = "Count unread users notifications.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping("/user/unread")
    public ResponseEntity<Long> countUnreadNotificationsForUser(@ApiIgnore @CurrentUser UserVO user) {
        Long unreadNotificationCount = notifiedUserService.countUnreadNotificationsForUser(user.getId());
        return ResponseEntity.ok(unreadNotificationCount);
    }

    @ApiOperation(value = "Set notification as read.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @PatchMapping("/{id}/user/read")
    public ResponseEntity<Void> setNotificationAsRead(
        @PathVariable Long id,
        @ApiIgnore @CurrentUser UserVO user
    ) {
        notifiedUserService.setNotificationAsRead(id, user.getId());
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Set notification as unread.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @PatchMapping("/{id}/user/unread")
    public ResponseEntity<Void> setNotificationAsUnRead(
        @PathVariable Long id,
        @ApiIgnore @CurrentUser UserVO user
    ) {
        notifiedUserService.setNotificationAsUnread(id, user.getId());
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Delete notification for user.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
    })
    @DeleteMapping("/{id}/user/delete")
    public ResponseEntity<Void> deleteNotificationForUser(
        @PathVariable Long id,
        @ApiIgnore @CurrentUser UserVO user
    ) {
        notifiedUserService.deleteByUserIdAndNotificationId(user.getId(), id);
        return ResponseEntity.ok().build();
    }

    /**
     * Method finds all notifications.
     *
     * @return Pageable of {@link NotificationDto}.
     * @author Nazar Klimovych
     */
    @ApiOperation(value = "Get all notifications.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/all")
    public ResponseEntity<PageableDto<NotificationDto>> getAllNotifications(@ApiIgnore Pageable pageable) {
        return ResponseEntity.ok(notificationService.findAll(pageable));
    }

    /**
     * Method finds all notifications generated by a user.
     *
     * @param id {@link Long} with needed user id.
     * @return Pageable of {@link NotificationDto}.
     * @author Nazar Klimovych
     */
    @ApiOperation(value = "Get all notifications generated by a user.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/sender/{id}/all")
    public ResponseEntity<PageableDto<NotificationDto>> getAllBySenderId(
        @ApiIgnore Pageable pageable,
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(notificationService.findAllBySenderId(pageable, id));
    }

    /**
     * Method finds specific notification by id.
     *
     * @param id {@link Long} with needed notification id.
     * @return {@link NotificationDto}.
     * @author Nazar Klimovych
     */
    @ApiOperation(value = "Get specific notification  by id.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/{id}")
    public ResponseEntity<NotificationDto> getNotificationById(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.findById(id));
    }

    /**
     * Method finds three last notifications received by the user.
     *
     * @param id {@link Long} with needed user id.
     * @return List of {@link NotificationDto}.
     * @author Nazar Klimovych
     */
    @ApiOperation(value = "Get three last notifications received by the user.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/user/{id}/lastThreeByUserId")
    public ResponseEntity<List<NotificationDto>> getThreeLastNotificationsByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.findThreeLastNotificationsByUserId(id));
    }

    /**
     * Method finds all notifications received by the user.
     *
     * @param id {@link Long} with needed user id.
     * @return Pageable of {@link NotificationDto}.
     * @author Nazar Klimovych
     */
    @ApiOperation(value = "Get all notifications received by the user.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/user/{id}/all")
    public ResponseEntity<PageableDto<NotificationDto>> getAllByNotifiedUserId(
            @ApiIgnore Pageable pageable,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(notificationService.findAllByNotifiedUserId(pageable, id));
    }

    /**
     * Method finds all notifications received by the user and specified by
     * the notification source.
     *
     * @param id {@link Long} with needed user id.
     * @param sourceId {@link Long} with needed source id.
     * @return Pageable of {@link NotificationDto}.
     * @author Nazar Klimovych
     */
    @ApiOperation(value = "Get all notifications received by the user and specified by the notification source.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/user/{id}/source/{sourceId}")
    public ResponseEntity<PageableDto<NotificationDto>> getAllByUserIdAndSourceId(
        @ApiIgnore Pageable pageable,
        @PathVariable Long id,
        @PathVariable Long sourceId
    ) {
        return ResponseEntity.ok(notificationService.findAllByUserIdAndSourceId(pageable, id, sourceId));
    }

    /**
     * Method finds all notifications specified by the notification source.
     *
     * @param id {@link Long} with needed source id.
     * @return Pageable of {@link NotificationDto}.
     * @author Nazar Klimovych
     */
    @ApiOperation(value = "Get all notifications specified by the notification source.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/source/{id}/all")
    public ResponseEntity<PageableDto<NotificationDto>> getAllBySourceId(
        @ApiIgnore Pageable pageable,
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(notificationService.findAllBySourceId(pageable, id));
    }

    /**
     * Method for deleting {@link NotificationDto} by its id.
     *
     * @param id {@link Long} with needed notification id.
     * @param user current {@link UserVO} that wants to delete.
     * @return id of deleted {@link NotificationDto}.
     * @author Nazar Klimovych
     */
    @ApiOperation(value = "Delete notification.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Object> deleteNotification(@PathVariable Long id, @ApiIgnore @CurrentUser UserVO user) {
        notificationService.delete(id, user);
        return ResponseEntity.ok().build();
    }
}

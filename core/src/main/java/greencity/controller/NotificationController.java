package greencity.controller;

import greencity.annotations.CurrentUser;
import greencity.constant.HttpStatuses;
import greencity.dto.notification.NotificationDto;
import greencity.dto.notification.NotificationDtoResponse;
import greencity.dto.user.UserVO;
import greencity.service.NotificationService;
import greencity.service.NotifiedUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    private final NotifiedUserService notifiedUserService;

    @Autowired
    public NotificationController(NotificationService notificationService, NotifiedUserService notifiedUserService) {
        this.notificationService = notificationService;
        this.notifiedUserService = notifiedUserService;
    }

    @ApiOperation(value = "Create EcoNewsComment notifications.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
    })
    @PostMapping("/econewscomment/{sourceId}")
    public ResponseEntity<List<NotificationDto>> save (@PathVariable Long sourceId){
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
    public ResponseEntity<List<NotificationDtoResponse>> getNotifications(@ApiIgnore @CurrentUser UserVO user) {
        List<NotificationDtoResponse> notifications = notificationService.getNotificationsForUser(user.getId());
        return ResponseEntity.ok(notifications);
    }

    @ApiOperation(value = "Count unread users notifications.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
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
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
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
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
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


}

package greencity.controller;

import greencity.annotations.ApiPageable;
import greencity.annotations.CurrentUser;
import greencity.constant.HttpStatuses;
import greencity.dto.PageableDto;
import greencity.dto.friends.UserFriendDto;
import greencity.dto.habit.HabitVO;
import greencity.dto.habitfact.HabitFactDto;
import greencity.dto.habitfact.HabitFactVO;
import greencity.dto.user.UserManagementDto;
import greencity.dto.user.UserVO;
import greencity.service.FriendService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/friends")
public class FriendController {
    private final FriendService friendService;

    /**
     * The controller which returns {@link FriendController}
     *
     * @param friendService {@link FriendService}.
     * @return {@link FriendController}.
     * @author Anisimov Eugene
     */
    @Autowired
    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    /**
     * Method which finds all friends.
     *
     * @param name   filter name.
     * @param userVO {@link UserVO} instance.
     * @return {@link ResponseEntity}.
     */
    @ApiOperation(value = "Find all friends")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @GetMapping
    @ApiPageable
    public ResponseEntity<PageableDto<UserFriendDto>> findAllFriendsOfUser(Pageable page,
        @RequestParam(required = false) String name,
        @ApiIgnore @CurrentUser UserVO userVO) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(friendService.findAllFriendsOfUser(userVO.getId(), name, page));
    }

    /**
     * Method which finds user all friends.
     *
     * @param userId user id.
     * @return {@link ResponseEntity}.
     */
    @ApiOperation(value = "Get all user's friends")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserManagementDto>> findUserFriendsByUserId(@PathVariable long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(friendService.findUserFriendsByUserId(userId));
    }

    /**
     * Method which deletes user friend.
     *
     * @param friendId friend id.
     * @param userVO   {@link UserVO} instance.
     * @return {@link ResponseEntity}.
     */
    @ApiOperation(value = "Delete user's friend")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })

    @DeleteMapping("/{friendId}")
    public ResponseEntity<ResponseEntity.BodyBuilder> deleteUserFriend(
        @ApiParam(value = "Id friend of current user. Cannot be empty.", required = true) @PathVariable long friendId,
        @ApiIgnore @CurrentUser UserVO userVO) {
        friendService.deleteUserFriendById(userVO.getId(), friendId);
        return ResponseEntity.ok().build();
    }
}

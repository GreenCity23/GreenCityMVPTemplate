package greencity.controller;

import greencity.annotations.ApiPageable;
import greencity.annotations.CurrentUser;
import greencity.constant.HttpStatuses;
import greencity.dto.PageableDto;
import greencity.dto.friends.UserFriendDto;
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
     * The controller which returns {@link FriendController}.
     *
     * @param friendService {@link FriendService}.
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
    public ResponseEntity<List<UserManagementDto>> findUserFriendsByUserId(@PathVariable Long userId) {
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
        @ApiParam(value = "Id friend of current user. Cannot be empty.", required = true) @PathVariable Long friendId,
        @ApiIgnore @CurrentUser UserVO userVO) {
        friendService.deleteUserFriendById(userVO.getId(), friendId);
        return ResponseEntity.ok().build();
    }

    /**
     * Method which return all users that are not friends of current user.
     *
     * @param userVO user.
     * @param name   filtering name.
     * @return {@link PageableDto} of {@link UserFriendDto}.
     */
    @ApiOperation(value = "Find all users that are not friend for current users")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/not-friends-yet")
    @ApiPageable
    public ResponseEntity<PageableDto<UserFriendDto>> findAllUsersExceptMainUserAndUsersFriend(
        @ApiIgnore Pageable page,
        @ApiIgnore @CurrentUser UserVO userVO,
        @RequestParam(required = false) String name) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(friendService.findAllUsersExceptMainUserAndUsersFriend(userVO.getId(), name, page));
    }

    /**
     * Method to add a new friend for the current user.
     *
     * @param friendId The ID of the friend to be added. Cannot be empty.
     * @param userVO   The current user.
     * @return A ResponseEntity with a status of OK.
     */
    @ApiOperation(value = "Add new user friend")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
    })
    @PostMapping("/{friendId}")
    public ResponseEntity<ResponseEntity.BodyBuilder> addNewFriend(
        @ApiParam("Id friend of current user. Cannot be empty.") @PathVariable Long friendId,
        @ApiIgnore @CurrentUser UserVO userVO) {
        friendService.addNewFriend(userVO.getId(), friendId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Method to accept a friend request from another user.
     *
     * @param friendId The ID of the friend whose request is being accepted. Cannot
     *                 be empty.
     * @param userVO   The current user.
     * @return A ResponseEntity with a status of OK.
     */
    @ApiOperation(value = "Accept friend request")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
    })
    @PatchMapping("/{friendId}/acceptFriend")
    public ResponseEntity<ResponseEntity.BodyBuilder> acceptFriendRequest(
        @ApiParam("Friend's id. Cannot be empty.") @PathVariable Long friendId,
        @ApiIgnore @CurrentUser UserVO userVO) {
        friendService.acceptFriendRequest(userVO.getId(), friendId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Method to decline a friend request from another user.
     *
     * @param friendId The ID of the friend whose request is being declined. Cannot
     *                 be empty.
     * @param userVO   The current user.
     * @return A ResponseEntity with a status of OK.
     */
    @ApiOperation(value = "Decline friend request")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),
    })
    @DeleteMapping("/{friendId}/declineFriend")
    public ResponseEntity<Object> declineFriendRequest(
        @ApiParam("Friend's id. Cannot be empty.") @PathVariable Long friendId,
        @ApiIgnore @CurrentUser UserVO userVO) {
        friendService.declineFriendRequest(userVO.getId(), friendId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Method to retrieve all the friend requests for a user.
     *
     * @param page   The pageable object containing pagination information.
     * @param userVO The current user.
     * @return A ResponseEntity containing a PageableDto of UserFriendDto objects,
     *         representing the friend requests for the user.
     */
    @ApiOperation(value = "Find user's requests")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/friendRequests")
    @ApiPageable
    public ResponseEntity<PageableDto<UserFriendDto>> getAllUserFriendsRequests(
        @ApiIgnore Pageable page,
        @ApiIgnore @CurrentUser UserVO userVO) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(friendService.getAllUserFriendRequests(userVO.getId(), page));
    }
}

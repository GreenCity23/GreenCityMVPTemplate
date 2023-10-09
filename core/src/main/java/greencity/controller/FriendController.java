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
import org.springframework.social.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * The FriendController class is a REST controller that handles requests related
 * to friends. It provides endpoints for adding, accepting, declining, and
 * deleting friends, as well as retrieving a user's friends, friend requests,
 * and users that are not friends yet.
 */
@RestController
@RequestMapping("/friends")
public class FriendController {
    private final FriendService friendService;

    /**
     * Create a new FriendController with the given FriendService.
     *
     * @param friendService - the FriendService to be used by the FriendController
     */
    @Autowired
    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    /**
     * Accepts a friend request.
     *
     * @param friendId the id of the friend to accept the request from
     * @param userVO   the current user
     * @return the response entity with a status of OK
     */
    @PatchMapping("/{friendId}/acceptFriend")
    @ApiOperation(value = "Accept friend request")
    @ApiResponses(value = {@ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),})
    public ResponseEntity<ResponseEntity.BodyBuilder> acceptFriendRequest(
        @ApiParam("Friend's id. Cannot be empty.") @PathVariable Long friendId, @ApiIgnore @CurrentUser UserVO userVO) {
        friendService.acceptFriendRequest(userVO.getId(), friendId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Method to add a new friend to the user's friend list.
     *
     * @param friendId The ID of the friend to be added. Cannot be empty.
     * @param userVO   The current user's information.
     * @return A ResponseEntity with the status code 200 for a successful addition
     *         or an error status code for invalid requests.
     */
    @PostMapping("/{friendId}")
    @ApiOperation(value = "Add new user friend")
    @ApiResponses(value = {@ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),})
    public ResponseEntity<ResponseEntity.BodyBuilder> addNewFriend(
        @ApiParam("Id friend of current user. Cannot be empty.") @PathVariable Long friendId,
        @ApiIgnore @CurrentUser UserVO userVO) {
        friendService.addNewFriend(userVO.getId(), friendId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Decline friend request.
     *
     * @param friendId Friend's id. Cannot be empty.
     * @param userVO   Current user's information.
     * @return ResponseEntity containing the response status.
     */
    @DeleteMapping("/{friendId}/declineFriend")
    @ApiOperation(value = "Decline friend request")
    @ApiResponses(value = {@ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND),})
    public ResponseEntity<Object> declineFriendRequest(
        @ApiParam("Friend's id. Cannot be empty.") @PathVariable Long friendId, @ApiIgnore @CurrentUser UserVO userVO) {
        friendService.declineFriendRequest(userVO.getId(), friendId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Deletes a friend from the user's friend list.
     *
     * @param friendId the id of the friend to be deleted (cannot be empty)
     * @param userVO   the current user object (ignored)
     * @return a ResponseEntity object with HttpStatus.OK if the friend is
     *         successfully deleted or an error message if there is a bad request,
     *         unauthorized access, or friend not found
     */
    @DeleteMapping("/{friendId}")
    @ApiOperation(value = "Delete user's friend")
    @ApiResponses(value = {@ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)})
    public ResponseEntity<ResponseEntity.BodyBuilder> deleteUserFriend(
        @ApiParam(value = "Id friend of current user. Cannot be empty.", required = true) @PathVariable Long friendId,
        @ApiIgnore @CurrentUser UserVO userVO) {
        friendService.deleteUserFriendById(userVO.getId(), friendId);
        return ResponseEntity.ok().build();
    }

    /**
     * Finds all the friends of the specified user.
     *
     * @param page   the Pageable object containing pagination information
     *               (optional)
     * @param name   the name of the user's friend to search for (optional)
     * @param userVO the current user object (ignored)
     * @return a ResponseEntity with the list of user's friends or an error message.
     */
    @GetMapping
    @ApiPageable
    @ApiOperation(value = "Find all friends")
    @ApiResponses(value = {@ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)})
    public ResponseEntity<PageableDto<UserFriendDto>> findAllFriendsOfUser(Pageable page,
        @RequestParam(required = false) String name, @ApiIgnore @CurrentUser UserVO userVO) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(friendService.findAllFriendsOfUser(userVO.getId(), name, page));
    }

    /**
     * Find all users that are not friends for the current user.
     *
     * @param page   the pageable object for pagination
     * @param userVO the current user
     * @param name   the name of the users to search (optional)
     * @return a ResponseEntity containing a PageableDto that represents the list of
     *         users
     */
    @GetMapping("/not-friends-yet")
    @ApiPageable
    @ApiOperation(value = "Find all users that are not friend for current users")
    @ApiResponses(value = {@ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)})
    public ResponseEntity<PageableDto<UserFriendDto>> findAllUsersExceptMainUserAndUsersFriend(@ApiIgnore Pageable page,
        @ApiIgnore @CurrentUser UserVO userVO, @RequestParam(required = false) String name) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(friendService.findAllUsersExceptMainUserAndUsersFriend(userVO.getId(), name, page));
    }

    /**
     * Finds all the friends of a user based on the provided user ID.
     *
     * @param userId The ID of the user whose friends are to be found.
     * @return A ResponseEntity containing a list of UserManagementDto objects
     *         representing the user's friends.
     * @throws ResourceNotFoundException if the user with the given ID does not
     *                                   exist.
     */
    @GetMapping("/user/{userId}")
    @ApiOperation(value = "Get all user's friends")
    @ApiResponses(value = {@ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)})
    public ResponseEntity<List<UserManagementDto>> findUserFriendsByUserId(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(friendService.findUserFriendsByUserId(userId));
    }

    /**
     * Get all user's friend requests.
     *
     * @param page   The pageable object containing pagination information.
     * @param userVO The UserVO object representing the logged in user.
     * @return ResponseEntity {@code <PageableDto<UserFriendDto>>} The
     *         ResponseEntity with the list of friend requests.
     */
    @GetMapping("/friendRequests")
    @ApiPageable
    @ApiOperation(value = "Find user's requests")
    @ApiResponses(value = {@ApiResponse(code = 200, message = HttpStatuses.OK),
        @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
        @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
        @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)})
    public ResponseEntity<PageableDto<UserFriendDto>> getAllUserFriendsRequests(@ApiIgnore Pageable page,
        @ApiIgnore @CurrentUser UserVO userVO) {
        return ResponseEntity.status(HttpStatus.OK).body(friendService.getAllUserFriendRequests(userVO.getId(), page));
    }
}
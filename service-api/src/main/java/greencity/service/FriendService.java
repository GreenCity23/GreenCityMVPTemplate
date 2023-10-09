package greencity.service;

import greencity.dto.PageableDto;
import greencity.dto.friends.UserFriendDto;
import greencity.dto.user.UserManagementDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FriendService {
    /**
     * Finds a list of friends for a user specified by user ID.
     *
     * @param userId User ID of the user to find friends for.
     * @return List of UserManagementDto representing friends of the user.
     * @author Yevhen Anisimov
     */
    List<UserManagementDto> findUserFriendsByUserId(Long userId);

    /**
     * Finds paged representation for friends of a user specified by user ID,
     * matching a given name pattern.
     *
     * @param id   User ID of the user to find friends for.
     * @param name Name pattern to match friends' names against.
     * @param page Paging and sorting information.
     * @return PageableDto of UserFriendDto representing friends of the user.
     * @author Yevhen Anisimov
     */
    PageableDto<UserFriendDto> findAllFriendsOfUser(Long id, String name, Pageable page);

    /**
     * Deletes a friend of a user specified by user ID and friend ID.
     *
     * @param id       User ID of the user to delete friend from.
     * @param friendId User ID of the friend to be removed.
     * @author Yevhen Anisimov
     */
    void deleteUserFriendById(Long id, Long friendId);

    /**
     * Finds all users that are not friends with the user specified by user ID.
     * Matches users against the given name pattern.
     *
     * @param id   User ID of the user to find non-friends for.
     * @param name Name pattern to match users' names against.
     * @param page Paging and sorting information.
     * @return PageableDto of UserFriendDto representing non-friends of the user.
     * @author Yevhen Anisimov
     */
    PageableDto<UserFriendDto> findAllUsersExceptMainUserAndUsersFriend(Long id, String name, Pageable page);

    /**
     * Adds a new friend to the user specified by user ID.
     *
     * @param id       The ID of the user to add a new friend to.
     * @param friendId The ID of the friend to be added.
     * @author Yevhen Anisimov
     */
    void addNewFriend(Long id, Long friendId);

    /**
     * Accepts a friend request from the user specified by user ID.
     *
     * @param id       The ID of the user accepting the friend request.
     * @param friendId The ID of the user who sent the friend request.
     * @author Yevhen Anisimov
     */
    void acceptFriendRequest(Long id, Long friendId);

    /**
     * Retrieves all friend requests for a given user.
     *
     * @param id   The ID of the user.
     * @param page The page details for pagination.
     * @return A {@link PageableDto} containing a list of {@link UserFriendDto}
     *         objects representing the friend requests.
     * @author Yevhen Anisimov
     */
    PageableDto<UserFriendDto> getAllUserFriendRequests(Long id, Pageable page);

    /**
     * Declines a friend request from a given user.
     *
     * @param id       The ID of the user who received the friend request.
     * @param friendId The ID of the user who sent the friend request.
     * @author Yevhen Anisimov
     */
    void declineFriendRequest(Long id, Long friendId);
}
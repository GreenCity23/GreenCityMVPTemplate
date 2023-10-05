package greencity.repository;

import greencity.dto.habit.HabitVO;
import greencity.dto.user.UserManagementVO;
import greencity.dto.user.UserVO;
import greencity.entity.User;
import greencity.repository.options.UserFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Repository for managing User entities.
 */
@Repository
public interface UserRepo extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    /**
     * Find {@link User} by email.
     *
     * @param email user email.
     * @return {@link User}
     */
    Optional<User> findByEmail(String email);

    /**
     * Find all {@link UserManagementVO}.
     *
     * @param filter   filter parameters
     * @param pageable pagination
     * @return list of all {@link UserManagementVO}
     */
    @Query(" SELECT new greencity.dto.user.UserManagementVO(u.id, u.name, u.email, u.userCredo, u.role, u.userStatus) "
        + " FROM User u ")
    Page<UserManagementVO> findAllManagementVo(UserFilter filter, Pageable pageable);

    /**
     * Find not 'DEACTIVATED' {@link User} by email.
     *
     * @param email - {@link User}'s email
     * @return found {@link User}
     * @author Vasyl Zhovnir
     */
    @Query("FROM User WHERE email=:email AND userStatus <> 1")
    Optional<User> findNotDeactivatedByEmail(String email);

    /**
     * Find id by email.
     *
     * @param email - User email
     * @return User id
     * @author Zakhar Skaletskyi
     */
    @Query("SELECT id FROM User WHERE email=:email")
    Optional<Long> findIdByEmail(String email);

    /**
     * Updates last activity time for a given user.
     *
     * @param userId               - {@link User}'s id
     * @param userLastActivityTime - new {@link User}'s last activity time
     * @author Yurii Zhurakovskyi
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE User SET last_activity_time=:userLastActivityTime WHERE id=:userId")
    void updateUserLastActivityTime(Long userId, Date userLastActivityTime);

    /**
     * Updates user status for a given user.
     *
     * @param userId     - {@link User}'s id
     * @param userStatus {@link String} - string value of user status to set
     */
    @Modifying
    @Transactional
    @Query("UPDATE User SET userStatus = CASE "
        + "WHEN (:userStatus = 'DEACTIVATED') THEN 1 "
        + "WHEN (:userStatus = 'ACTIVATED') THEN 2 "
        + "WHEN (:userStatus = 'CREATED') THEN 3 "
        + "WHEN (:userStatus = 'BLOCKED') THEN 4 "
        + "ELSE 0 END "
        + "WHERE id = :userId")
    void updateUserStatus(Long userId, String userStatus);

    /**
     * Find the last activity time by {@link User}'s id.
     *
     * @param userId - {@link User}'s id
     * @return {@link Date}
     */
    @Query(nativeQuery = true,
        value = "SELECT last_activity_time FROM users WHERE id=:userId")
    Optional<Timestamp> findLastActivityTimeById(Long userId);

    /**
     * Updates user rating as event organizer.
     *
     * @param userId {@link User}'s id
     * @param rate   new {@link User}'s rating as event organizer
     * @author Danylo Hlynskyi
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE User SET eventOrganizerRating=:rate WHERE id=:userId")
    void updateUserEventOrganizerRating(Long userId, Double rate);

    /**
     * Retrieves the list of the user's friends (which have INPROGRESS assign to the
     * habit).
     *
     * @param habitId {@link HabitVO} id.
     * @param userId  {@link UserVO} id.
     * @return List of friends.
     */
    @Query(nativeQuery = true, value = "SELECT * FROM ((SELECT user_id FROM users_friends AS uf "
        + "WHERE uf.friend_id = :userId AND uf.status = 'FRIEND' AND "
        + "(SELECT count(*) FROM habit_assign ha WHERE ha.habit_id = :habitId AND ha.user_id = uf.user_id "
        + "AND ha.status = 'INPROGRESS') = 1) "
        + "UNION "
        + "(SELECT friend_id FROM users_friends AS uf "
        + "WHERE uf.user_id = :userId AND uf.status = 'FRIEND' AND "
        + "(SELECT count(*) FROM habit_assign ha WHERE ha.habit_id = :habitId AND ha.user_id = uf.friend_id "
        + "AND ha.status = 'INPROGRESS') = 1)) as ui JOIN users as u ON user_id = u.id")
    List<User> getFriendsAssignedToHabit(Long userId, Long habitId);

    /**
     * Get all user friends.
     *
     * @param userId The ID of the user.
     *
     * @return list of {@link User}.
     */
    @Query(nativeQuery = true, value = "SELECT * FROM users WHERE id IN ( "
        + "(SELECT user_id FROM users_friends WHERE friend_id = :userId and status = 'FRIEND')"
        + "UNION (SELECT friend_id FROM users_friends WHERE user_id = :userId and status = 'FRIEND'));")
    List<User> getAllUserFriends(Long userId);

    /**
     * Get all user friends for pageable.
     *
     * @param userId        The ID of the user
     * @param filteringName Name of user
     * @param pageable      pageable
     *
     * @return page of {@link User}.
     * @author Yevhen Anisimov
     */
    @Query(nativeQuery = true, value = "SELECT * FROM users u "
        + "WHERE u.id IN ("
        + "      SELECT user_id AS id FROM users_friends WHERE friend_id = :userId AND status = 'FRIEND' "
        + "      UNION "
        + "      SELECT friend_id AS id FROM users_friends WHERE user_id = :userId AND status = 'FRIEND' "
        + ") AND LOWER(u.name) LIKE LOWER(CONCAT('%', :filteringName, '%'))")
    Page<User> findAllFriendsOfUser(Long userId, String filteringName, Pageable pageable);

    /**
     * Delete user friend.
     *
     * @param userId   The ID of the user, @param friendId.
     * @param friendId The ID of the user friend.
     * @author Yevhen Anisimov
     */
    @Modifying
    @Query(nativeQuery = true,
        value = "DELETE FROM users_friends WHERE (user_id = :userId AND friend_id = :friendId)"
            + " OR (user_id = :friendId AND friend_id = :userId)")
    void deleteUserFriend(Long userId, Long friendId);

    /**
     * Retrieve a page of users who are not the main user and are not friends with the main user.
     *
     * @param userId        The ID of the main user.
     * @param filteringName The name to be used for filtering. The users retrieved will have names that contain the filteringName, case-insensitive.
     * @param pageable      The pageable object used for pagination and sorting.
     * @return A Page object containing the list of users who meet the filter criteria.
     * @author Yevhen Anisimov
     */
    @Query(nativeQuery = true, value = "SELECT * FROM users u "
            + "WHERE u.id != :userId "
            + "AND u.id NOT IN ("
            + "      SELECT user_id AS id FROM users_friends WHERE friend_id = :userId AND status = 'FRIEND' "
            + "      UNION "
            + "      SELECT friend_id AS id FROM users_friends WHERE user_id = :userId AND status = 'FRIEND' " + ") AND (LOWER(u.name) LIKE LOWER(CONCAT('%', :filteringName, '%')) OR LOWER(u.first_name) LIKE LOWER(CONCAT('%', :filteringName, '%'))) ")
    Page<User> getAllUsersExceptMainUserAndFriends(Long userId, String filteringName, Pageable pageable);

    /**
     * Adds a new friend for a given user.
     *
     * @param userId   the ID of the user who wants to add a new friend
     * @param friendId the ID of the user to be added as a friend
     * @author Yevhen Anisimov
     */
    @Modifying
    @Query(nativeQuery = true,
            value = "INSERT INTO users_friends(user_id, friend_id, status, created_date) "
                    + "VALUES (:userId, :friendId, 'REQUEST', CURRENT_TIMESTAMP)")
    void addNewFriend(Long userId, Long friendId);

    /**
     * Accepts a friend request for a given user.
     *
     * @param userId   the ID of the user who received the friend request
     * @param friendId the ID of the user who sent the friend request
     * @author Yevhen Anisimov
     */
    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE users_friends SET status = 'FRIEND' "
                    + "WHERE user_id = :friendId AND friend_id = :userId")
    void acceptFriendRequest(Long userId, Long friendId);

    /**
     * Retrieves all friend requests for a given user.
     *
     * @param userId    the ID of the user for whom to retrieve friend requests
     * @param pageable  the pagination information
     * @return a Page containing the list of friend requests
     * @author Yevhen Anisimov
     */
    @Query(nativeQuery = true, value = "SELECT u.* FROM users u "
            + "       INNER JOIN (SELECT DISTINCT uf.user_id FROM users_friends uf"
            + "                  WHERE uf.friend_id = :userId AND uf.status = 'REQUEST'"
            + "                  GROUP BY uf.user_id) AS uf ON u.id = uf.user_id")
    Page<User> getAllUserFriendRequests(Long userId, Pageable pageable);

    /**
     * Declines a friend request between two users.
     *
     * @param userId    the ID of the user who received the friend request
     * @param friendId  the ID of the user who sent the friend request
     * @author Yevhen Anisimov
     */
    @Modifying
    @Query(nativeQuery = true,
            value = "DELETE FROM users_friends WHERE user_id = :friendId AND friend_id = :userId")
    void declineFriendRequest(Long userId, Long friendId);
}
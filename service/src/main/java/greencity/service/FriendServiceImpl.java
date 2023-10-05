package greencity.service;

import greencity.dto.PageableDto;
import greencity.dto.friends.UserFriendDto;
import greencity.dto.user.UserManagementDto;
import greencity.entity.User;
import greencity.exception.exceptions.NotFoundException;
import greencity.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static greencity.constant.ErrorMessage.USER_NOT_FOUND_BY_ID;

/**
 * Implements FriendService and provides methods for working with friends.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class FriendServiceImpl implements FriendService {
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final EntityManager entityManager;

    /**
     * Validates if a user exists based on their ID.
     *
     * @param userId The ID of the user to validate.
     * @throws NotFoundException If the user does not exist.
     */
    private void validateUserExistById(Long userId) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException(USER_NOT_FOUND_BY_ID + userId);
        }
    }

    /**
     * Retrieves a list of user friends based on the given userId.
     *
     * @param userId the ID of the user whose friends are being retrieved
     * @return a list of UserManagementDto objects representing the user's friends
     * @throws NotFoundException if the user does not exist
     */
    @Override
    public List<UserManagementDto> findUserFriendsByUserId(Long userId) {
        validateUserExistById(userId);

        return userRepo.getAllUserFriends(userId).stream()
                .map(friend -> modelMapper.map(friend, UserManagementDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a pageable list of friends of a user with the given ID, filtered by name.
     *
     * @param id    The ID of the user.
     * @param name  The name to filter friends by.
     * @param page  The page information for pagination.
     * @return A PageableDto containing the list of UserFriendDto objects, the total number of elements,
     *         the current page number, and the total number of pages.
     */
    @Override
    public PageableDto<UserFriendDto> findAllFriendsOfUser(Long id, String name, Pageable page) {
        name = (name == null) ? "" : name;
        Page<User> userPage = userRepo.findAllFriendsOfUser(id, name, page);
        if (page.getPageNumber() >= userPage.getTotalPages()) {
            return new PageableDto<>(
                    new ArrayList<>(),
                    0,
                    0,
                    0);
        }
        List<UserFriendDto> userFriendDtos = queryUserFriendDtos(id, name, userPage.getContent());

        return new PageableDto<>(
                userFriendDtos,
                userPage.getTotalElements(),
                userPage.getPageable().getPageNumber(),
                userPage.getTotalPages());
    }

    /**
     *
     */
    private List<UserFriendDto> queryUserFriendDtos(Long id, String name, List<User> userContent) {
        TypedQuery<UserFriendDto> query = entityManager
                .createNamedQuery("User.getAllUsersFriends",
                        UserFriendDto.class);
        query.setParameter("userId", id);
        query.setParameter("friends", userContent);
        query.setParameter("name", name);
        return query.getResultList();
    }

    /**
     * Generates a list of user friend DTOs, excluding the main user and their friends, via a named query
     *
     * @param id          ID of the main user
     * @param name        Name of the friend user
     * @param userContent Content of the user page
     * @return List of UserFriendDto
     */
    private List<UserFriendDto> queryNotUserFriendDtos(Long id, String name, List<User> userContent) {
        TypedQuery<UserFriendDto> query = entityManager
                .createNamedQuery("User.getAllUsersExceptMainUserAndFriends",
                        UserFriendDto.class);
        query.setParameter("userId", id);
        query.setParameter("notFriends", userContent);
        query.setParameter("name", name);
        return query.getResultList();
    }

    /**
     * Deletes a user friend by their ID.
     *
     * @param id       - ID of the main user
     * @param friendId - ID of the friend user to be deleted
     */
    @Override
    public void deleteUserFriendById(Long id, Long friendId) {
        validateUserExistById(friendId);

        userRepo.deleteUserFriend(id, friendId);
    }

    /**
     * Returns a pageable list of users excluding the main user and their friends.
     *
     * @param id   ID of the main user
     * @param name Name of the user (optional)
     * @param page Pageable object for pagination
     * @return PageableDto containing a list of user friend DTOs
     */
    @Override
    public PageableDto<UserFriendDto> findAllUsersExceptMainUserAndUsersFriend(Long id, String name, Pageable page) {
        name = name == null ? "" : name;

        Page<User> userPage =
                userRepo.getAllUsersExceptMainUserAndFriends(id, name, page);
        if (page.getPageNumber() >= userPage.getTotalPages()) {
            return new PageableDto<>(
                    new ArrayList<>(),
                    0,
                    0,
                    0);
        }
        return new PageableDto<>(
                queryNotUserFriendDtos(id, name, userPage.getContent()),
                userPage.getTotalElements(),
                userPage.getPageable().getPageNumber(),
                userPage.getTotalPages());
    }

    /**
     * Adds a new friend for a user.
     *
     * @param id       The ID of the user.
     * @param friendId The ID of the friend to be added.
     */
    @Override
    public void addNewFriend(Long id, Long friendId) {
        validateUserExistById(friendId);
        validateUserExistById(id);
        userRepo.addNewFriend(id, friendId);
    }

    /**
     * Accepts a friend request between two users.
     *
     * @param id        the ID of the user accepting the friend request
     * @param friendId  the ID of the user who sent the friend request
     */
    @Override
    public void acceptFriendRequest(Long id, Long friendId) {
        validateUserExistById(friendId);
        validateUserExistById(id);
        userRepo.acceptFriendRequest(id, friendId);
    }

    /**
     * Retrieves all friend requests sent to a user.
     *
     * @param id The ID of the user.
     * @param page The page number and size for pagination of the results.
     * @return A PageableDto containing a list of UserFriendDto objects representing the friend requests,
     *         the total number of friend requests, the current page number, and the total number of pages.
     *         Returns an empty list if no friend requests are found.
     * @throws IllegalArgumentException If the user ID is null or negative.
     */
    @Override
    public PageableDto<UserFriendDto> getAllUserFriendRequests(Long id, Pageable page) {
        validateUserExistById(id);

        Page<User> users = userRepo.getAllUserFriendRequests(id, page);

        List<UserFriendDto> userFriendDtoList = users.getContent().stream()
                .map(i -> modelMapper.map(i, UserFriendDto.class))
                .collect(Collectors.toList());


        return new PageableDto<>(
                userFriendDtoList,
                users.getTotalElements(),
                users.getPageable().getPageNumber(),
                users.getTotalPages());
    }

    /**
     * Declines a friend request from a user.
     *
     * @param id       The ID of the user who is declining the friend request.
     * @param friendId The ID of the user who sent the friend request.
     */
    @Override
    public void declineFriendRequest(Long id, Long friendId) {
        validateUserExistById(friendId);
        validateUserExistById(id);
        userRepo.declineFriendRequest(id, friendId);
    }
}
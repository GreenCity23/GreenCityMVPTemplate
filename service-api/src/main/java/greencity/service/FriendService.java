package greencity.service;

import greencity.dto.PageableDto;
import greencity.dto.friends.UserFriendDto;
import greencity.dto.user.UserManagementDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FriendService {

    /**
     * Methhod for finding friends by userId.
     *
     * @author Yevhen Anisimov
     */

    List<UserManagementDto> findUserFriendsByUserId(long userId);

    /**
     * Methhod for finding all friends by userId.
     *
     * @author Yevhen Anisimov
     */

    PageableDto<UserFriendDto> findAllFriendsOfUser(Long id, String name, Pageable page);

    /**
     * Methhod for deleting all friends by userId.
     *
     * @author Yevhen Anisimov
     */

    void deleteUserFriendById(Long id, long friendId);
}

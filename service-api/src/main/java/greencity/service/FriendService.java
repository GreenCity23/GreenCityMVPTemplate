package greencity.service;

import greencity.dto.PageableDto;
import greencity.dto.friends.UserFriendDto;
import greencity.dto.user.UserManagementDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FriendService {
    List<UserManagementDto> findUserFriendsByUserId(long userId);

    PageableDto<UserFriendDto> findAllFriendsOfUser(Long id, String name, Pageable page);

    void deleteUserFriendById(Long id, long friendId);
}

package greencity.service;

import greencity.ModelUtils;
import greencity.dto.PageableDto;
import greencity.dto.friends.UserFriendDto;
import greencity.dto.user.UserManagementDto;
import greencity.entity.User;
import greencity.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FriendServiceImplTest {
    @Mock
    TypedQuery<UserFriendDto> mockQuery;
    @InjectMocks
    private FriendServiceImpl friendService;
    @Mock
    private UserRepo userRepo;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private EntityManager entityManager;

    @Test
    public void testFindUserFriendsByUserId() {
        Long userId = 1L;

        when(userRepo.existsById(userId)).thenReturn(true);
        when(userRepo.getAllUserFriends(userId)).thenReturn(new ArrayList<>());

        List<UserManagementDto> result = friendService.findUserFriendsByUserId(userId);

        assertNotNull(result);
        verify(userRepo, times(1)).existsById(userId);
        verify(userRepo, times(1)).getAllUserFriends(userId);
    }

    @Test
    public void testFindAllFriendsOfUser() {
        Long userId = 1L;
        String name = "FriendName";
        int page = 0;
        int size = 1;
        long totalElements = 50;
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = new PageImpl<>(List.of(ModelUtils.getUser()), pageable, totalElements);
        when(userRepo.existsById(userId)).thenReturn(true);
        when(userRepo.findAllFriendsOfUser(userId, name, pageable)).thenReturn(userPage);
        when(entityManager.createNamedQuery("User.getAllUsersFriends", UserFriendDto.class))
            .thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);

        PageableDto<UserFriendDto> result = friendService.findAllFriendsOfUser(userId, name, pageable);

        assertNotNull(result);
        verify(userRepo).existsById(userId);
        verify(userRepo, times(1)).findAllFriendsOfUser(userId, name, pageable);
    }

    @Test
    public void testDeleteUserFriendById() {
        Long userId = 1L;
        Long friendId = 2L;
        when(userRepo.existsById(userId)).thenReturn(true);
        when(userRepo.existsById(friendId)).thenReturn(true);
        doNothing().when(userRepo).deleteUserFriend(userId, friendId);

        assertDoesNotThrow(() -> friendService.deleteUserFriendById(userId, friendId));
        verify(userRepo, times(1)).deleteUserFriend(userId, friendId);
    }

    @Test
    public void findAllUsersExceptMainUserAndUsersFriendTest() {
        long userId = 1L;
        int page = 0;
        int size = 1;
        long totalElements = 50;
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = new PageImpl<>(List.of(ModelUtils.getUser()), pageable, totalElements);
        String name = "zhenya";

        when(userRepo.existsById(userId)).thenReturn(true);
        when(userRepo.getAllUsersExceptMainUserAndFriends(userId, name, pageable)).thenReturn(userPage);
        when(entityManager.createNamedQuery("User.getAllUsersExceptMainUserAndFriends", UserFriendDto.class))
            .thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(new ArrayList<>());

        PageableDto<UserFriendDto> pageableDto =
            friendService.findAllUsersExceptMainUserAndUsersFriend(userId, name, pageable);

        assertNotNull(pageableDto);
        verify(userRepo).existsById(userId);
        verify(userRepo).getAllUsersExceptMainUserAndFriends(userId, name, pageable);
    }

    @Test
    public void testAddNewFriend() {
        Long userId = 1L;
        Long friendId = 2L;

        when(userRepo.existsById(userId)).thenReturn(true);
        when(userRepo.existsById(friendId)).thenReturn(true);

        assertDoesNotThrow(() -> friendService.addNewFriend(userId, friendId));
        verify(userRepo, times(1)).existsById(userId);
        verify(userRepo, times(1)).existsById(friendId);
    }

    @Test
    public void testAcceptFriendRequest() {
        Long userId = 1L;
        Long friendId = 2L;

        when(userRepo.existsById(userId)).thenReturn(true);
        when(userRepo.existsById(friendId)).thenReturn(true);

        assertDoesNotThrow(() -> friendService.acceptFriendRequest(userId, friendId));
        verify(userRepo, times(1)).existsById(userId);
        verify(userRepo, times(1)).existsById(friendId);
    }

    @Test
    public void testGetAllUserFriendRequests() {
        Long userId = 1L;
        int page = 0;
        int size = 1;
        long totalElements = 50;
        Pageable pageable = PageRequest.of(page, size);

        Page<User> userPage = new PageImpl<>(List.of(ModelUtils.getUser()), pageable, totalElements);

        when(userRepo.existsById(userId)).thenReturn(true);
        when(userRepo.getAllUserFriendRequests(userId, pageable)).thenReturn(userPage);

        PageableDto<UserFriendDto> result = friendService.getAllUserFriendRequests(userId, pageable);

        assertNotNull(result);
        verify(userRepo, times(1)).getAllUserFriendRequests(userId, pageable);
    }

    @Test
    public void testDeclineFriendRequest() {
        Long userId = 1L;
        Long friendId = 2L;

        when(userRepo.existsById(userId)).thenReturn(true);
        when(userRepo.existsById(friendId)).thenReturn(true);

        assertDoesNotThrow(() -> friendService.declineFriendRequest(userId, friendId));
        verify(userRepo, times(1)).existsById(userId);
        verify(userRepo, times(1)).existsById(friendId);
    }
}

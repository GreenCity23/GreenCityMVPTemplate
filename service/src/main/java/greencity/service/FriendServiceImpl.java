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

@Service
@RequiredArgsConstructor
@Transactional
public class FriendServiceImpl implements FriendService {
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final EntityManager entityManager;

    @Override
    public List<UserManagementDto> findUserFriendsByUserId(long userId) {
        if (!userRepo.existsById(userId)) {
            throw new NotFoundException(USER_NOT_FOUND_BY_ID + userId);
        }
        return userRepo.getAllUserFriends(userId).stream()
            .map(friend -> modelMapper.map(friend, UserManagementDto.class)).collect(Collectors.toList());
    }

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
        TypedQuery<UserFriendDto> query = entityManager
            .createNamedQuery("User.getAllUsersFriends",
                UserFriendDto.class);
        query.setParameter("userId", id);
        query.setParameter("friends", userPage.getContent());
        query.setParameter("name", name);
        List<UserFriendDto> userFriendDtos = query.getResultList();
        userFriendDtos.forEach(friend -> friend.setFriendStatus("FRIEND"));
        return new PageableDto<>(
            userFriendDtos,
            userPage.getTotalElements(),
            userPage.getPageable().getPageNumber(),
            userPage.getTotalPages());
    }

    @Override
    public void deleteUserFriendById(Long id, long friendId) {
        if (!userRepo.existsById(friendId)) {
            throw new NotFoundException(USER_NOT_FOUND_BY_ID + friendId);
        }
        userRepo.deleteUserFriend(id, friendId);
    }
}

package greencity.service;

import greencity.constant.ErrorMessage;
import greencity.dto.notifieduser.NotifiedUserDto;
import greencity.entity.NotifiedUser;
import greencity.exception.exceptions.NotFoundException;
import greencity.mapping.NotifiedUserDtoMapper;
import greencity.repository.NotifiedUserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotifiedUserServiceImpl implements NotifiedUserService {
    private final NotifiedUserRepo notifiedUserRepo;
    private final NotifiedUserDtoMapper notifiedUserDtoMapper;
    private final ModelMapper modelMapper;

    @Override
    public List<NotifiedUserDto> getAllNotifiedUsers() {
        List<NotifiedUser> notifiedUsers = notifiedUserRepo.findAll();
        return notifiedUsers.stream()
                .map(notifiedUserDtoMapper::convert)
                .collect(Collectors.toList());
    }

    @Override
    public NotifiedUserDto getNotifiedUserById(Long id) {
        Optional<NotifiedUser> notifiedUser = notifiedUserRepo.findById(id);
        return modelMapper.map(notifiedUser, NotifiedUserDto.class);
    }

    @Override
    public NotifiedUserDto saveNotifiedUsers(NotifiedUserDto notifiedUserDto) {
        NotifiedUser notifiedUser = modelMapper.map(notifiedUserDto, NotifiedUser.class);
        NotifiedUser savedNotifiedUser = notifiedUserRepo.save(notifiedUser);
        return modelMapper.map(savedNotifiedUser, NotifiedUserDto.class);
    }

    @Override
    public List<NotifiedUserDto> getAllUsersNotifications(Long userId) {
        List<NotifiedUser> userNotifications = notifiedUserRepo.findAllByUserId(userId);
        return userNotifications.stream()
                .map(notifiedUserDtoMapper::convert)
                .collect(Collectors.toList());
    }

    @Override
    public Long countUnreadNotificationsForUser(Long userId) {
        return notifiedUserRepo.countByUserIdAndIsReadIsFalse(userId);
    }

    @Override
    public void setNotificationAsRead(Long notificationId, Long userId) {
        if (notifiedUserRepo.countByUserIdAndNotificationId(userId, notificationId) == 0L){
            throw new NotFoundException(ErrorMessage.NOTIFICATION_NOT_FOUND_FOR_USER);
        }
        notifiedUserRepo.setNotificationAsRead(notificationId, userId);
    }

    @Override
    public void setNotificationAsUnread(Long notificationId, Long userId) {
        if (notifiedUserRepo.countByUserIdAndNotificationId(userId, notificationId) == 0L){
            throw new NotFoundException(ErrorMessage.NOTIFICATION_NOT_FOUND_FOR_USER);
        }
        notifiedUserRepo.setNotificationAsUnread(notificationId, userId);
    }

    public void deleteByUserIdAndNotificationId(Long userId, Long notificationId){
        if (notifiedUserRepo.countByUserIdAndNotificationId(userId, notificationId) == 0L){
            throw new NotFoundException(ErrorMessage.NOTIFICATION_NOT_FOUND_FOR_USER);
        }
        notifiedUserRepo.deleteByUserIdAndNotificationId(userId, notificationId);
    }

    @Override
    public void deleteNotifiedUser(Long id) {
        notifiedUserRepo.deleteById(id);
    }
}

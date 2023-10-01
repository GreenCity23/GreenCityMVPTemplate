package greencity.service;

import greencity.dto.notifieduser.NotifiedUserDto;
import greencity.entity.NotifiedUser;
import greencity.mapping.NotifiedUserDtoMapper;
import greencity.repository.NotifiedUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotifiedUserServiceImpl implements NotifiedUserService {
    private final NotifiedUserRepo notifiedUserRepo;
    private final NotifiedUserDtoMapper notifiedUserDtoMapper;

    @Autowired
    public NotifiedUserServiceImpl(NotifiedUserRepo notifiedUserRepo, NotifiedUserDtoMapper notifiedUserDtoMapper) {
        this.notifiedUserRepo = notifiedUserRepo;
        this.notifiedUserDtoMapper = notifiedUserDtoMapper;
    }

    @Override
    public List<NotifiedUserDto> getAllNotifiedUsers() {
        List<NotifiedUser> notifiedUsers = notifiedUserRepo.findAll();
        return notifiedUsers.stream()
                .map(notifiedUserDtoMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<NotifiedUserDto> getNotifiedUserById(Long id) {
        Optional<NotifiedUser> notifiedUserOptional = notifiedUserRepo.findById(id);
        return notifiedUserOptional.map(notifiedUserDtoMapper::convertToDto);
    }

    @Override
    public NotifiedUserDto saveNotifiedUsers(NotifiedUserDto notifiedUserDto) {
        NotifiedUser notifiedUser = notifiedUserDtoMapper.convertToEntity(notifiedUserDto);
        NotifiedUser savedNotifiedUser = notifiedUserRepo.save(notifiedUser);
        return notifiedUserDtoMapper.convertToDto(savedNotifiedUser);
    }

    @Override
    public List<NotifiedUserDto> getAllUsersNotifications(Long userId) {
        List<NotifiedUser> userNotifications = notifiedUserRepo.findAllByUserId(userId);
        return userNotifications.stream()
                .map(notifiedUserDtoMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Long countUnreadNotificationsForUser(Long userId) {
        return notifiedUserRepo.countByUserIdAndIsReadIsFalse(userId);
    }

    @Override
    public void setNotificationAsRead(Long notificationId, Long userId) {
        notifiedUserRepo.setNotificationAsRead(notificationId, userId);
    }

    @Override
    public void setNotificationAsUnread(Long notificationId, Long userId) {
        notifiedUserRepo.setNotificationAsUnread(notificationId, userId);
    }

    @Override
    public void deleteNotifiedUser(Long id) {
        notifiedUserRepo.deleteById(id);
    }
}

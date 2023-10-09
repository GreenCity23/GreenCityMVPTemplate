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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NotifiedUserDto> getAllNotifiedUsers() {
        List<NotifiedUser> notifiedUsers = notifiedUserRepo.findAll();
        return notifiedUsers.stream()
                .map(notifiedUserDtoMapper::convert)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NotifiedUserDto getNotifiedUserById(Long id) {
        Optional<NotifiedUser> notifiedUser = notifiedUserRepo.findById(id);
        return modelMapper.map(notifiedUser, NotifiedUserDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NotifiedUserDto saveNotifiedUsers(NotifiedUserDto notifiedUserDto) {
        NotifiedUser notifiedUser = modelMapper.map(notifiedUserDto, NotifiedUser.class);
        NotifiedUser savedNotifiedUser = notifiedUserRepo.save(notifiedUser);
        return modelMapper.map(savedNotifiedUser, NotifiedUserDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long countUnreadNotificationsForUser(Long userId) {
        return notifiedUserRepo.countByUserIdAndIsReadIsFalse(userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNotificationAsRead(Long notificationId, Long userId) {
        if (notifiedUserRepo.countByUserIdAndNotificationId(userId, notificationId) == 0L){
            throw new NotFoundException(ErrorMessage.NOTIFICATION_NOT_FOUND_FOR_USER);
        }
        notifiedUserRepo.setNotificationAsRead(notificationId, userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNotificationAsUnread(Long notificationId, Long userId) {
        if (notifiedUserRepo.countByUserIdAndNotificationId(userId, notificationId) == 0L){
            throw new NotFoundException(ErrorMessage.NOTIFICATION_NOT_FOUND_FOR_USER);
        }
        notifiedUserRepo.setNotificationAsUnread(notificationId, userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteByUserIdAndNotificationId(Long userId, Long notificationId){
        if (notifiedUserRepo.countByUserIdAndNotificationId(userId, notificationId) == 0L){
            throw new NotFoundException(ErrorMessage.NOTIFICATION_NOT_FOUND_FOR_USER);
        }
        notifiedUserRepo.deleteByUserIdAndNotificationId(userId, notificationId);
    }
}

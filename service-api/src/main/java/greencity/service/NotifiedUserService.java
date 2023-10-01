package greencity.service;



import greencity.dto.notifieduser.NotifiedUserDto;

import java.util.List;
import java.util.Optional;

public interface NotifiedUserService {
    List<NotifiedUserDto> getAllNotifiedUsers();

    Optional<NotifiedUserDto> getNotifiedUserById(Long id);

    NotifiedUserDto saveNotifiedUsers(NotifiedUserDto notifiedUserDto);

    void deleteNotifiedUser(Long id);
}

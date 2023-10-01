package greencity.service;

import greencity.dto.notifieduser.NotifiedUserDto;
import greencity.mapping.NotifiedUserDtoMapper;
import greencity.repository.NotifiedUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotifiedUserServiceImpl implements NotifiedUserService {
    private final NotifiedUserRepo notifiedUserRepo;
    private final NotifiedUserDtoMapper notifiedUsersMapper;

    @Autowired
    public NotifiedUserServiceImpl(NotifiedUserRepo notifiedUserRepo, NotifiedUserDtoMapper notifiedUserMapper) {
        this.notifiedUserRepo = notifiedUserRepo;
        this.notifiedUsersMapper = notifiedUserMapper;
    }

    @Override
    public List<NotifiedUserDto> getAllNotifiedUsers() {
        return null;
    }

    @Override
    public Optional<NotifiedUserDto> getNotifiedUserById(Long id) {
        return null;
    }

    @Override
    public NotifiedUserDto saveNotifiedUsers(NotifiedUserDto notifiedUsersDto) {
        return null;
    }

    @Override
    public void deleteNotifiedUser(Long id) {
        notifiedUserRepo.deleteById(id);
    }
}

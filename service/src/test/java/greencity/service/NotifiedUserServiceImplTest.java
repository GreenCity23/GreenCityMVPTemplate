package greencity.service;

import greencity.dto.notifieduser.NotifiedUserDto;
import greencity.entity.NotifiedUser;
import greencity.mapping.NotifiedUserDtoMapper;
import greencity.repository.NotifiedUserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class NotifiedUserServiceImplTest {
    private NotifiedUserServiceImpl notifiedUserService;

    @Mock
    private NotifiedUserRepo notifiedUserRepo;

    @Mock
    private NotifiedUserDtoMapper notifiedUserDtoMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        notifiedUserService = new NotifiedUserServiceImpl(notifiedUserRepo, notifiedUserDtoMapper, new ModelMapper());
    }

    @Test
    public void testGetAllNotifiedUsers() {
        List<NotifiedUser> notifiedUsers = new ArrayList<>();

        when(notifiedUserRepo.findAll()).thenReturn(notifiedUsers);

        List<NotifiedUserDto> expectedDtos = new ArrayList<>();

        for (NotifiedUser notifiedUser : notifiedUsers) {
            NotifiedUserDto dto = new NotifiedUserDto();
            // Set properties on dto based on notifiedUser
            expectedDtos.add(dto);
        }

        when(notifiedUserDtoMapper.convert(any(NotifiedUser.class)))
                .thenReturn(expectedDtos.get(0), expectedDtos.subList(1, expectedDtos.size()).toArray(new NotifiedUserDto[0]));

        List<NotifiedUserDto> actualDtos = notifiedUserService.getAllNotifiedUsers();

        assertEquals(expectedDtos, actualDtos);

        verify(notifiedUserRepo, times(1)).findAll();
        verify(notifiedUserDtoMapper, times(notifiedUsers.size())).convert(any(NotifiedUser.class));
    }

    @Test
    public void testGetNotifiedUserById() {
        Long userId = 1L;
        Optional<NotifiedUser> notifiedUserOptional = Optional.of(new NotifiedUser());
        NotifiedUserDto expectedDto = new NotifiedUserDto();

        when(notifiedUserRepo.findById(userId)).thenReturn(notifiedUserOptional);
        when(notifiedUserDtoMapper.convert(notifiedUserOptional.get())).thenReturn(expectedDto);

        NotifiedUserDto actualDtoOptional = notifiedUserService.getNotifiedUserById(userId);

        assertEquals(Optional.of(expectedDto), actualDtoOptional);

        verify(notifiedUserRepo, times(1)).findById(userId);
        verify(notifiedUserDtoMapper, times(1)).convert(notifiedUserOptional.get());
    }

    @Test
    public void testSaveNotifiedUsers() {
        NotifiedUserDto inputDto = new NotifiedUserDto();
        inputDto.setId(1L);
        inputDto.setIsRead(false);

        NotifiedUser expectedEntity = new NotifiedUser();
        expectedEntity.setId(1L);
        expectedEntity.setIsRead(false);

//        when(notifiedUserDtoMapper.convert(inputDto)).thenReturn(expectedEntity);

        when(notifiedUserRepo.save(expectedEntity)).thenReturn(expectedEntity);

        when(notifiedUserDtoMapper.convert(expectedEntity)).thenReturn(inputDto);

        NotifiedUserDto resultDto = notifiedUserService.saveNotifiedUsers(inputDto);

        assertEquals(inputDto, resultDto);

        verify(notifiedUserRepo, times(1)).save(expectedEntity);

//        verify(notifiedUserDtoMapper, times(1)).convert(inputDto);
        verify(notifiedUserDtoMapper, times(1)).convert(expectedEntity);
    }

}

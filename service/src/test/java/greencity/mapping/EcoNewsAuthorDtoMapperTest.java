package greencity.mapping;

import greencity.dto.user.EcoNewsAuthorDto;
import greencity.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EcoNewsAuthorDtoMapperTest {

    @InjectMocks
    private EcoNewsAuthorDtoMapper mapper;
    @Mock
    private User user;

    @Test
    void convert() {
        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.when(user.getName()).thenReturn("Amanda");

        EcoNewsAuthorDto authorDto = mapper.convert(user);

        Assertions.assertEquals(1L, authorDto.getId());
        Assertions.assertEquals("Amanda", authorDto.getName());
    }
}

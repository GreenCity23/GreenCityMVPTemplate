package greencity.mapping;

import greencity.dto.notifieduser.NotifiedUserDto;
import greencity.entity.NotifiedUser;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

@Component
public class NotifiedUserDtoMapper {
    private final ModelMapper modelMapper;

    public NotifiedUserDtoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public NotifiedUserDto convertToDto(NotifiedUser notifiedUser) {
        return modelMapper.map(notifiedUser, NotifiedUserDto.class);
    }

    public NotifiedUser convertToEntity(NotifiedUserDto notifiedUserDto) {
        return modelMapper.map(notifiedUserDto, NotifiedUser.class);
    }
}

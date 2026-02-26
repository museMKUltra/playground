package roller.playground.mappers;

import org.mapstruct.Mapper;
import roller.playground.dtos.RegisterRequest;
import roller.playground.dtos.UserDto;
import roller.playground.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(RegisterRequest request);

    UserDto toDto(User user);
}

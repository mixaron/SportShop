package ryabchuk.sportshop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ryabchuk.sportshop.dto.UserDto;
import ryabchuk.sportshop.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    User toEntity(UserDto userDTO);

    UserDto toDto(User user);

    List<UserDto> toDtoList(List<User> users);
}

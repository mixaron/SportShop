package ryabchuk.sportshop.mapper;

import org.mapstruct.Mapper;
import ryabchuk.sportshop.dto.UserDto;
import ryabchuk.sportshop.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDto userDTO);

    UserDto toDto(User user);

    List<UserDto> toDtoList(List<User> users);
}

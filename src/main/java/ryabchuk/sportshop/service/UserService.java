package ryabchuk.sportshop.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ryabchuk.sportshop.dto.UserDto;
import ryabchuk.sportshop.mapper.UserMapper;
import ryabchuk.sportshop.model.User;
import ryabchuk.sportshop.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void register(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Пользователь уже существует");
        }
        User user = userMapper.toEntity(userDto);
        userRepository.save(user);
    }
}

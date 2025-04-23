package ryabchuk.sportshop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ryabchuk.sportshop.model.user.Address;
import ryabchuk.sportshop.model.user.User;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    @Email(message = "Введите корректный email")
    @NotBlank(message = "Email обязателен")
    private String email;

    @Size(min = 8, message = "Пароль должен содержать не менее 8 символов")
    private String password;

    private String currentPassword;

    @NotBlank(message = "Имя обязательно")
    @Size(min = 3, max = 20, message = "Имя должно быть длинее трех символов, и меньше 20 символов")
    private String firstName;

    @NotBlank(message = "Фамилия обязательна")
    @Size(min = 3, max = 20, message = "Фамилия должно быть длинее трех символов, и меньше 20 символов")
    private String lastName;

    private Address address;

    private User.Role role;

    private String telegramChatId;
}

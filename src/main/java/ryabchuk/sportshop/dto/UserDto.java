package ryabchuk.sportshop.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ryabchuk.sportshop.model.user.Address;
import ryabchuk.sportshop.model.user.User;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    @Email
    private String email;

    private String password;

    private String currentPassword;

    private String firstName;

    private String lastName;

    private Address address;

    private User.Role role;

    private String telegramChatId;
}

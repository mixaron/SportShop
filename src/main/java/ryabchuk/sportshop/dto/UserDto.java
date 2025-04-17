package ryabchuk.sportshop.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ryabchuk.sportshop.model.Address;

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
}

package ryabchuk.sportshop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDTO(
        @NotBlank @Email String email,
        @NotBlank String password,
        String telegramChatId
) {}

package ryabchuk.sportshop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddressDto {
    @NotBlank(message = "Регион обязателен")
    private String region;

    @NotBlank(message = "Город обязателен")
    private String city;

    @NotBlank(message = "Улица обязательна")
    private String street;

    @NotBlank(message = "Дом обязателен")
    private String house;

    private String apartment;

    @NotBlank(message = "Почтовый индекс обязателен")
    @Size(min = 6, max = 6, message = "Индекс должен состоять из шести цифр")
    private String postalCode;
}


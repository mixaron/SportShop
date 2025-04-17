package ryabchuk.sportshop.dto;

import lombok.Data;

@Data
public class AddressDto {
    private String region;
    private String city;
    private String street;
    private String house;
    private String apartment;
    private String postalCode;
}


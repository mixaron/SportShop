package ryabchuk.sportshop.mapper;

import org.mapstruct.Mapper;
import ryabchuk.sportshop.dto.AddressDto;
import ryabchuk.sportshop.model.Address;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toEntity(AddressDto addressDto);
    AddressDto toDto(Address address);
    List<AddressDto> toDtoList(List<Address> addresses);
}


package ryabchuk.sportshop.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ryabchuk.sportshop.dto.AddressDto;
import ryabchuk.sportshop.mapper.AddressMapper;
import ryabchuk.sportshop.model.Address;
import ryabchuk.sportshop.model.User;
import ryabchuk.sportshop.repository.AddressRepository;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final UserService userService;

    public void addAddress(AddressDto addressDto, Long userId) {
        if (addressRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException("User already have address");
        }
        Address address = addressMapper.toEntity(addressDto);
        User user  = userService.getUserById(userId);
        address.setUser(user);

        addressRepository.save(address);
    }

    public void editAddress(AddressDto addressDto, Long userId) {
        Address address = addressRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));

        address.setApartment(addressDto.getApartment());
        address.setCity(addressDto.getCity());
        address.setHouse(addressDto.getHouse());
        address.setStreet(addressDto.getStreet());
        address.setPostalCode(addressDto.getPostalCode());
        address.setRegion(addressDto.getRegion());

        addressRepository.save(address);
    }

    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }

    public AddressDto getAddress(Long userId) {
        Address address = addressRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));
        return addressMapper.toDto(address);
    }
}

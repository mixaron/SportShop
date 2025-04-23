package ryabchuk.sportshop.service.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ryabchuk.sportshop.dto.AddressDto;
import ryabchuk.sportshop.mapper.AddressMapper;
import ryabchuk.sportshop.model.user.Address;
import ryabchuk.sportshop.model.user.User;
import ryabchuk.sportshop.repository.user.AddressRepository;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final UserService userService;

    public void addAddress(AddressDto addressDto, Long userId) {
        if (addressRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException("User already has address");
        }

        Address address = addressMapper.toEntity(addressDto);
        User user = userService.getUserById(userId);

        address.setUser(user);
        user.setAddress(address);
        userService.saveUser(user);
    }


    public void editAddress(AddressDto addressDto, Long userId) {
        Address address = addressRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));

        updateAddressFromDto(address, addressDto);
        addressRepository.save(address);
    }

    @Transactional
    public void deleteAddressByUserId(Long userId) {
        Address address = addressRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));

        User user = address.getUser();
        user.setAddress(null);

        addressRepository.delete(address);
    }

    public AddressDto getAddress(Long userId) {
        Address address = addressRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));
        return addressMapper.toDto(address);
    }

    private void updateAddressFromDto(Address address, AddressDto dto) {
        address.setApartment(dto.getApartment());
        address.setCity(dto.getCity());
        address.setHouse(dto.getHouse());
        address.setStreet(dto.getStreet());
        address.setPostalCode(dto.getPostalCode());
        address.setRegion(dto.getRegion());
    }

}

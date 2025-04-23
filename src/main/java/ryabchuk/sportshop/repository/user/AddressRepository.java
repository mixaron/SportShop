package ryabchuk.sportshop.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ryabchuk.sportshop.model.user.Address;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    boolean existsByUserId(Long userId);

    Optional<Address> findByUserId(Long userId);

}

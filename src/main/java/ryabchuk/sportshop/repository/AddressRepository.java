package ryabchuk.sportshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ryabchuk.sportshop.model.Address;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    boolean existsByUserId(Long userId);

    Optional<Address> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}

package ryabchuk.sportshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ryabchuk.sportshop.model.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
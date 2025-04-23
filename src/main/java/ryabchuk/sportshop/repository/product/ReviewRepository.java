package ryabchuk.sportshop.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ryabchuk.sportshop.model.product.Review;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByUserIdAndProductId(Long userId, Long productId);

    List<Review> findAllByProductIdAndStatus(Long productId, Review.Status status);

    List<Review> findAllByStatus(Review.Status status);
}

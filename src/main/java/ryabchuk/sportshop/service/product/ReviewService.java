package ryabchuk.sportshop.service.product;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ryabchuk.sportshop.model.product.Product;
import ryabchuk.sportshop.model.product.Review;
import ryabchuk.sportshop.model.user.User;
import ryabchuk.sportshop.repository.product.ReviewRepository;
import ryabchuk.sportshop.service.user.TelegramService;
import ryabchuk.sportshop.service.user.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final ProductService productService;
    private final TelegramService telegramService;

    public void addReview(Review review, Long userId, Long productId) {
        User user = userService.getUserById(userId);
        Product product = productService.getProductById(productId);
        review.setProduct(product);
        review.setUser(user);
        reviewRepository.save(review);
    }

    public void editReview(Review review, Long userId, Long productId) {
        Review existingReview = reviewRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        existingReview.setRating(review.getRating());
        existingReview.setComment(review.getComment());
        existingReview.setStatus(Review.Status.PROCESSING);

        reviewRepository.save(existingReview);
    }

    public Optional<Review> findUserReviewForProduct(Long userId, Long productId) {
        return reviewRepository.findByUserIdAndProductId(userId, productId);
    }

    public void deleteReview(Long userId, Long productId) {
        Optional<Review> review = reviewRepository.findByUserIdAndProductId(userId, productId);
        review.ifPresent(reviewRepository::delete);
    }

    public List<Review> getAllProcessingReviews() {
        return reviewRepository.findAllByStatus(Review.Status.PROCESSING);
    }

    public void changeStatus(Long reviewId, Review.Status status) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
        review.setStatus(status);

        reviewRepository.save(review);

        telegramService.notifyUserByEmail(review.getUser().getId(),
                "Статус вашего отзыва " + review.getProduct().getName() + " изменён на: " + status.getLabel());
    }

    public List<Review> getApprovedReviewsByProduct(Long productId) {
        return reviewRepository.findAllByProductIdAndStatus(productId, Review.Status.APPROVED);
    }
}

package ryabchuk.sportshop.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ryabchuk.sportshop.model.Product;
import ryabchuk.sportshop.model.Review;
import ryabchuk.sportshop.model.User;
import ryabchuk.sportshop.repository.ReviewRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final ProductService productService;

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

        reviewRepository.save(existingReview);
    }

    public Optional<Review> findUserReviewForProduct(Long userId, Long productId) {
        return reviewRepository.findByUserIdAndProductId(userId, productId);
    }

    public void deleteReview(Long userId, Long productId) {
        Optional<Review> review = reviewRepository.findByUserIdAndProductId(userId, productId);
        review.ifPresent(reviewRepository::delete);
    }
}

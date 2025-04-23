package ryabchuk.sportshop.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ryabchuk.sportshop.config.user.CustomUserDetails;
import ryabchuk.sportshop.model.product.Review;
import ryabchuk.sportshop.service.product.ReviewService;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public String addReview(@RequestParam Long productId,
                            @ModelAttribute Review review,
                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        reviewService.addReview(review, userDetails.getId(), productId);
        return "redirect:/products/" + productId;
    }

    @PostMapping("/edit")
    public String editReview(@RequestParam Long productId,
                             @ModelAttribute Review review,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        reviewService.editReview(review, userDetails.getId(), productId);
        return "redirect:/products/" + productId;
    }

    @PostMapping("/delete/{productId}")
    public String deleteReview(@PathVariable Long productId,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        reviewService.deleteReview(userDetails.getId(), productId);
        return "redirect:/products/" + productId;
    }

}

package ryabchuk.sportshop.controller.moderator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ryabchuk.sportshop.model.product.Review;
import ryabchuk.sportshop.service.product.ReviewService;

@Controller
@RequestMapping("/moderator/reviews")
@RequiredArgsConstructor
public class ModeratorReviewController {
    private final ReviewService reviewService;

    @GetMapping
    public String reviewsPage(Model model) {
        model.addAttribute("reviews", reviewService.getAllProcessingReviews());
        return "moderator/reviews/view";
    }

    @PostMapping
    public String changeReviewStatus(@RequestParam Long reviewId, @RequestParam Review.Status status) {
        reviewService.changeStatus(reviewId, status);
        return "redirect:/moderator/reviews";
    }
}

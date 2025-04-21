package ryabchuk.sportshop.controller.product;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ryabchuk.sportshop.config.user.CustomUserDetails;
import ryabchuk.sportshop.model.Product;
import ryabchuk.sportshop.model.Review;
import ryabchuk.sportshop.service.CategoryService;
import ryabchuk.sportshop.service.OrderItemService;
import ryabchuk.sportshop.service.ProductService;
import ryabchuk.sportshop.service.ReviewService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderItemService orderItemService;
    private final ReviewService reviewService;

    @GetMapping
    public String listProducts(@RequestParam(required = false) Long categoryId,
                               @RequestParam(required = false) String query,
                               Model model) {
        List<Product> products = categoryId != null
                ? productService.filterByCategory(categoryId)
                : query != null
                ? productService.searchProducts(query)
                : productService.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "products/list";
    }

@GetMapping("/{productId}")
public String viewProduct(@AuthenticationPrincipal CustomUserDetails userDetails,
                         @PathVariable Long productId, Model model) {
    
    Product product = productService.getProductById(productId);
    model.addAttribute("product", product);

    model.addAttribute("approvedReviews", reviewService.getApprovedReviewsByProduct(productId));
    
    boolean isAuthenticated = userDetails != null;
    model.addAttribute("isAuthenticated", isAuthenticated);

    if (isAuthenticated) {
        boolean isUserBuy = orderItemService.isUserBuyProduct(userDetails.getId(), productId);
        model.addAttribute("isUserBuy", isUserBuy);

        Optional<Review> userReview = reviewService.findUserReviewForProduct(userDetails.getId(), productId);
        model.addAttribute("hasUserReview", userReview.isPresent());
        model.addAttribute("userReview", userReview.orElse(new Review()));
    } else {
        model.addAttribute("isUserBuy", false);
        model.addAttribute("hasUserReview", false);
    }

    model.addAttribute("review", new Review());

    return "products/view";
}


    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product.getImage() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(product.getImage());
        }
        return ResponseEntity.notFound().build();
    }
}

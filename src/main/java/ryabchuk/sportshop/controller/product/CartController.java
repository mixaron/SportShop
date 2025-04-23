package ryabchuk.sportshop.controller.product;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ryabchuk.sportshop.config.user.CustomUserDetails;
import ryabchuk.sportshop.service.order.CartService;

import java.util.Map;

@Controller
@RequestMapping("/cart")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public String viewCart(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.addAttribute("cartItems", cartService.getCart(userDetails.getId()));
        return "cart/view";
    }

    @PostMapping("/add")
    @ResponseBody
    public Map<String, String> addToCart(@RequestParam Long productId,
                                         @RequestParam int quantity,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        cartService.addToCart(userDetails.getId(), productId, quantity);
        return Map.of("status", "success", "message", "Товар добавлен в корзину");
    }


    @PostMapping("/update")
    public String updateCart(@RequestParam Long productId,
                             @RequestParam int quantity,
                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        cartService.updateCartItem(userDetails.getId(), productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long productId,
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        cartService.removeFromCart(userDetails.getId(), productId);
        return "redirect:/cart";
    }
}

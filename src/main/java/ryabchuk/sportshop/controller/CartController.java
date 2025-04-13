package ryabchuk.sportshop.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ryabchuk.sportshop.config.CustomUserDetails;
import ryabchuk.sportshop.service.CartService;
import ryabchuk.sportshop.service.UserService;

@Controller
@RequestMapping("/cart")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public String viewCart(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        model.addAttribute("cartItems", cartService.getCart(userId));
        return "cart/view";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam int quantity,
                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        cartService.addToCart(userDetails.getId(), productId, quantity);
        return "redirect:/cart";
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

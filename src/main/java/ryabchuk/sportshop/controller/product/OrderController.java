package ryabchuk.sportshop.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ryabchuk.sportshop.config.user.CustomUserDetails;
import ryabchuk.sportshop.service.OrderService;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public String viewOrders(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                             Model model) {
        model.addAttribute("orders", orderService.getOrdersByUserId(customUserDetails.getId()));
        return "order/list";
    }

    @PostMapping("/buy")
    public String createOrder(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        orderService.createFakeOrder(customUserDetails.getId());
        return "redirect:/order";
    }
}

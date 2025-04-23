package ryabchuk.sportshop.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ryabchuk.sportshop.model.order.Order;
import ryabchuk.sportshop.model.order.OrderItem;
import ryabchuk.sportshop.model.user.User;
import ryabchuk.sportshop.repository.order.OrderRepository;
import ryabchuk.sportshop.service.user.TelegramService;
import ryabchuk.sportshop.service.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UserService userService;
    private final TelegramService telegramService;

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findAllByUserId(userId);
    }

    @Transactional
    public void createFakeOrder(Long userId) {
        User user  = userService.getUserById(userId);
        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.DELIVERED);

        List<OrderItem> items = cartService.getCart(userId).stream().map(cartItem -> {
            OrderItem item = new OrderItem();
            item.setProduct(cartItem.getProduct());
            item.setQuantity(cartItem.getQuantity());
            item.setOrder(order);
            return item;
        }).collect(Collectors.toList());

        order.setItems(items);
        cartService.removeFromCartById(userId);
        orderRepository.save(order);

        telegramService.notifyUserByEmail(userId, "Статус вашего заказа №"
                + order.getId() + " изменён на: " + order.getStatus().getDisplayName());
    }

    public boolean userHasDeliveredOder(Long userId, Long orderId) {
        return orderRepository.existsByIdAndUserIdAndStatus(
                orderId, userId, Order.OrderStatus.DELIVERED);
    }

    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }
}

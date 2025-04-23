package ryabchuk.sportshop.service.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ryabchuk.sportshop.model.order.Order;
import ryabchuk.sportshop.model.order.OrderItem;
import ryabchuk.sportshop.repository.order.OrderItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public boolean isUserBuyProduct(Long userId, Long productId) {
        List<OrderItem> orderItems = orderItemRepository.findAllByProductId(productId);
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getOrder().getUser().getId().equals(userId)
                    && orderItem.getOrder().getStatus() == Order.OrderStatus.DELIVERED) {
                return true;
            }
        }
        return false;
    }
}

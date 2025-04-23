package ryabchuk.sportshop.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ryabchuk.sportshop.model.order.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByIdAndUserIdAndStatus(Long id, Long userId, Order.OrderStatus status);
    List<Order> findAllByUserId(Long userId);
}

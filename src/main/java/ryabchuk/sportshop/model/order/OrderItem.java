package ryabchuk.sportshop.model.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ryabchuk.sportshop.model.product.Product;

@Entity
@Table(name = "order_item")
@Setter
@Getter
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Product product;

    private int quantity;
}

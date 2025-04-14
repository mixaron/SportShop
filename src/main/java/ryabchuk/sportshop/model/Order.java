package ryabchuk.sportshop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Setter
@Getter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @Getter
    public enum OrderStatus {
        CREATED("Создан"),
        PAID("Оплачен"),
        SHIPPED("Отправлен"),
        DELIVERED("Доставлен");

        private final String displayName;

        OrderStatus(String displayName) {
            this.displayName = displayName;
        }

    }

}

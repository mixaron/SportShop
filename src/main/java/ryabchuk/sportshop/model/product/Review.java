package ryabchuk.sportshop.model.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ryabchuk.sportshop.model.user.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Product product;

    @Min(value = 1, message = "Оценка должна быть не меньше 1")
    @Max(value = 5, message = "Оценка должна быть не больше 5")
    private int rating;

    @NotBlank(message = "Комментарий обязателен")
    private String comment;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PROCESSING;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @Getter
    @RequiredArgsConstructor
    public enum Status {
        PROCESSING("На рассмотрении"),
        APPROVED("Одобрен"),
        REJECTED("Отклонен");

        private final String label;
    }
}
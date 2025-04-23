package ryabchuk.sportshop.model.product;

import jakarta.persistence.*;
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

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;

    private int rating;

    private String comment;

    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
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


package com.walletguardians.walletguardiansapi.domain.expenses.entity;

import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private Long id;

    @Column(nullable = false)
    private int amount;

    @Column // null일 수 있음
    private String description;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, name = "store_name")
    private String storeName;

    @Column(name = "img_path") // null일 수 있음
    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // User 엔티티와의 관계

    @Builder
    private Expense(Long id, int amount, String description, Date date, String category, String storeName, String imagePath) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.category = category;
        this.storeName = storeName;
        this.imagePath = imagePath;
    }

    public void update(Expense updateExpense) {
        this.category = updateExpense.category;
        this.amount = updateExpense.amount;
        this.storeName = updateExpense.storeName;
        this.description = updateExpense.description;
    }
}

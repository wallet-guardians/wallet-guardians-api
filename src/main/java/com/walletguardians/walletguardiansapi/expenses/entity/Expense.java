package com.walletguardians.walletguardiansapi.expenses.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // jpa가 해당 엔터티 정보에 접근하는데, reflection api 사용하는데 기본생성자 필요
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private Long id;

    private int amount;

    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private String category;

    private String storeName;

    private String imagePath;

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

package com.walletguardians.walletguardiansapi.expenses.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // jpa가 해당 엔터티 정보에 접근하는데, reflection api 사용하는데 기본생성자 필요
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private Long id;

    private int amount;

    private String description;

    private String date;

    private String storeName;

    private String imagePath;

    @Builder
    private Expense(Long id, int amount, String description, String date, String storeName, String imagePath) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.storeName = storeName;
        this.imagePath = imagePath;
    }
}

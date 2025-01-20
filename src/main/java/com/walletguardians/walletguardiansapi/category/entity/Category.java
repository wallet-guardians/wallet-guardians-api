package com.walletguardians.walletguardiansapi.category.entity;

import com.walletguardians.walletguardiansapi.expenses.entity.Expense;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private long id;

    private String categoryName;

    @OneToMany(mappedBy = "category") // 나중에 쓸지도 모르니 양방향 매핑
    private List<Expense> expenses = new ArrayList<>();

    @Builder
    public Category(Long id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

}

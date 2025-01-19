package com.walletguardians.walletguardiansapi.category.repository;

import com.walletguardians.walletguardiansapi.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

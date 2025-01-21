package com.walletguardians.walletguardiansapi.domain.category.repository;

import com.walletguardians.walletguardiansapi.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

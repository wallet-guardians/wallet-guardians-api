package com.walletguardians.walletguardiansapi.domain.category.controller.dto.response;

import com.walletguardians.walletguardiansapi.domain.category.entity.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryResponse {

    private Long id;

    private String categoryName;

    @Builder
    public CategoryResponse(Long id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    public static CategoryResponse from(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .build();
    }
}

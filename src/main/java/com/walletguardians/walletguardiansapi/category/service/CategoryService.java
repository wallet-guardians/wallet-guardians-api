package com.walletguardians.walletguardiansapi.category.service;

import com.walletguardians.walletguardiansapi.category.controller.dto.response.CategoryResponse;
import com.walletguardians.walletguardiansapi.category.entity.Category;
import com.walletguardians.walletguardiansapi.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse> categoryResponses = new ArrayList<>();
        for (Category category : categories) {
            categoryResponses.add(CategoryResponse.from(category));
        }
        return categoryResponses;
    }
}

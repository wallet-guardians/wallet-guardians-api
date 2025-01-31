package com.walletguardians.walletguardiansapi.domain.category.controller;

import com.walletguardians.walletguardiansapi.domain.category.controller.dto.response.CategoryResponse;
import com.walletguardians.walletguardiansapi.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // 클라이언트에게 카테고리에 대한 정보를 주기 위한
    // 카테고리 조회 API
    @GetMapping()
    public List<CategoryResponse> getCategories() {
        return categoryService.getCategories();
    }

}

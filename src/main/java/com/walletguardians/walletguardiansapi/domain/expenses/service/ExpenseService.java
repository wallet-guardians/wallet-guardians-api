package com.walletguardians.walletguardiansapi.domain.expenses.service;

import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateExpenseRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateReceiptDTO;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.UpdateExpenseRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.response.ExpenseResponse;
import com.walletguardians.walletguardiansapi.domain.expenses.repository.ExpenseRepository;
import com.walletguardians.walletguardiansapi.domain.expenses.entity.Expense;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    // 파일 저장할 위치
    @Value(value = "${spring.file.dir}")
    private String fileDir;

    // 지출 생성
    public void createExpense(Date date, CreateExpenseRequest createExpenseRequest) {
        Expense expense = createExpenseRequest.toEntity();
        expense.setDate(date);
        expenseRepository.save(expense);
    }

    // 지출 조회
    public List<ExpenseResponse> getExpenses(Date date) {
        List<Expense> expenses = expenseRepository.findAll();
        List<ExpenseResponse> expenseResponses = new ArrayList<>();
        for (Expense expense : expenses) {
            if (expense.getDate().equals(date)) {
                expenseResponses.add(ExpenseResponse.from(expense));
            }
        }
        return expenseResponses;
    }

    // 지출 수정
    public void updateExpense(Long id, UpdateExpenseRequest updateExpenseRequest) {
        Expense updateExpense = updateExpenseRequest.toEntity();
        Expense findExpense = expenseRepository.findById(id)
                .orElseThrow(() ->new IllegalArgumentException("Expense not found"));
        findExpense.update(updateExpense);
    }

    // ID로 지출 삭제
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    // 로컬에 파일 저장
    public String createReceiptExpense(MultipartFile receiptFile) {
        if (receiptFile.isEmpty()) {
            return "파일이 없습니다.";
        }

        String fullPath = "";
        try {
            // 파일 저장 경로 설정
            String fileDir = "C:/Users/ahyeu/Documents/images"; // 경로 지정
            File directory = new File(fileDir);
            // 디렉토리가 없으면 생성
            if (!directory.exists()) {
                directory.mkdirs();
            }
            // 고유한 파일 이름 생성
            String uniqueFileName = UUID.randomUUID().toString() + "_" + receiptFile.getOriginalFilename();
            fullPath = fileDir + "/" + uniqueFileName;
            // 파일 저장
            receiptFile.transferTo(new File(fullPath));
        } catch (IOException e) {
            e.printStackTrace();
            return "파일 저장 실패: " + e.getMessage();
        }

        return "파일 저장 성공: " + fullPath;
    }

}

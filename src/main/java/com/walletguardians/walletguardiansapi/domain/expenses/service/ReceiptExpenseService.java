package com.walletguardians.walletguardiansapi.domain.expenses.service;

import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateReceiptRequest;
import com.walletguardians.walletguardiansapi.global.auth.CustomUserDetails;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface ReceiptExpenseService {

    void createReceiptExpense(MultipartFile file, CreateReceiptRequest dto) throws IOException;

    void uploadReceipt(MultipartFile receiptFile, CreateReceiptRequest dto, CustomUserDetails customUserDetails);

}

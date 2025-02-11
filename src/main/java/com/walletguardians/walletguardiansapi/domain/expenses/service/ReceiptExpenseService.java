package com.walletguardians.walletguardiansapi.domain.expenses.service;

import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateReceiptRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.service.dto.FileInfo;
import com.walletguardians.walletguardiansapi.domain.expenses.service.dto.OcrResponse;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import com.walletguardians.walletguardiansapi.global.auth.CustomUserDetails;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface ReceiptExpenseService {

    void createReceiptExpense(FileInfo fileInfo, OcrResponse ocrResponse, CreateReceiptRequest dto, User user) throws IOException;

    FileInfo uploadReceipt(MultipartFile receiptFile, CreateReceiptRequest dto, CustomUserDetails customUserDetails);

}

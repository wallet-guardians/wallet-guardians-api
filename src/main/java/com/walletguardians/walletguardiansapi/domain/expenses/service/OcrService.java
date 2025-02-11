package com.walletguardians.walletguardiansapi.domain.expenses.service;

import com.walletguardians.walletguardiansapi.domain.expenses.service.dto.OcrResponse;
import java.io.IOException;

public interface OcrService {
    OcrResponse sendOcrRequest(String contentType, String imagePath) throws IOException;
}

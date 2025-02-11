package com.walletguardians.walletguardiansapi.global.util;

import com.walletguardians.walletguardiansapi.domain.expenses.service.dto.OcrResponse;
import java.util.Objects;

public class OcrUtil {

    public static int extractPrice(OcrResponse ocrResponse) {
        if (ocrResponse == null || ocrResponse.getImages() == null) {
            return 0;
        }
        return ocrResponse.getImages().stream()
                .map(OcrResponse.ImageResult::getReceipt)
                .filter(Objects::nonNull)
                .map(OcrResponse.ImageResult.Receipt::getResult)
                .filter(Objects::nonNull)
                .map(OcrResponse.ImageResult.Receipt.ReceiptResult::getTotalPrice)
                .filter(Objects::nonNull)
                .map(OcrResponse.ImageResult.Receipt.ReceiptResult.TotalPrice::getPrice)
                .filter(Objects::nonNull)
                .map(OcrResponse.ImageResult.CommonField::getText)
                .map(OcrUtil::parsePrice)
                .findFirst()
                .orElse(0);
    }

    private static int parsePrice(String priceText) {
        if (priceText == null || priceText.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(priceText.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static String extractStoreName(OcrResponse ocrResponse) {
        if (ocrResponse == null || ocrResponse.getImages() == null) {
            return "";
        }

        return ocrResponse.getImages().stream()
                .map(OcrResponse.ImageResult::getReceipt)
                .filter(Objects::nonNull)
                .map(OcrResponse.ImageResult.Receipt::getResult)
                .filter(Objects::nonNull)
                .map(OcrResponse.ImageResult.Receipt.ReceiptResult::getStoreInfo)
                .filter(Objects::nonNull)
                .map(storeInfo -> {
                    String name = storeInfo.getName() != null ? storeInfo.getName().getText() : "";
                    String subName = storeInfo.getSubName() != null ? storeInfo.getSubName().getText() : "";
                    return (name + " " + subName).trim();
                })
                .findFirst()
                .orElse("");
    }
}

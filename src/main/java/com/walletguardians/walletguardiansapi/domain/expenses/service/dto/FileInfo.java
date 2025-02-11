package com.walletguardians.walletguardiansapi.domain.expenses.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileInfo {
    private String filePath;
    private String contentType;

    public static FileInfo of(String filePath, String contentType) {
        return FileInfo.builder()
                .filePath(filePath)
                .contentType(contentType)
                .build();
    }
}
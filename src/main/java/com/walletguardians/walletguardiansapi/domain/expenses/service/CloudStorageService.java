package com.walletguardians.walletguardiansapi.domain.expenses.service;

import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateReceiptRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.service.dto.FileInfo;
import com.walletguardians.walletguardiansapi.global.auth.CustomUserDetails;
import org.springframework.web.multipart.MultipartFile;

public interface CloudStorageService {

    FileInfo uploadPicture(MultipartFile pictureFile, String pictureType,
        CreateReceiptRequest dto, CustomUserDetails customUserDetails);

    FileInfo uploadProfilePicture(MultipartFile pictureFile, String pictureType,
        CustomUserDetails customUserDetails);

    void deletePicture(String pictureName, String pictureType, String email);

    String getBucketName();

}

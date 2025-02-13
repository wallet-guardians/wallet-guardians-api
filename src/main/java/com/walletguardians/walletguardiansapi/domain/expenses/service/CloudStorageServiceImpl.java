package com.walletguardians.walletguardiansapi.domain.expenses.service;

import com.google.cloud.WriteChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateReceiptRequest;
import com.walletguardians.walletguardiansapi.domain.expenses.service.dto.FileInfo;
import com.walletguardians.walletguardiansapi.global.security.CustomUserDetails;
import com.walletguardians.walletguardiansapi.global.exception.BaseException;
import com.walletguardians.walletguardiansapi.global.response.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CloudStorageServiceImpl implements CloudStorageService {

    private final Storage storage;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    @Transactional
    @Override
    public FileInfo uploadPicture(MultipartFile pictureFile, String pictureType,
            CreateReceiptRequest dto, CustomUserDetails customUserDetails) {
        validateFile(pictureFile);

        String filePath = generateFilePath(pictureFile, pictureType, dto, customUserDetails);
        String contentType = pictureFile.getContentType();

        uploadToCloudStorage(pictureFile, filePath, contentType);

        if ("receipts".equals(pictureType)) {
            replaceReceiptMetadata(filePath, dto);
        }

        return createFileInfo(filePath, getFileFormat(pictureFile));
    }

    private String getFileFormat(MultipartFile pictureFile) {
        String contentType = pictureFile.getContentType();
        return contentType.split("/")[1];
    }

    @Transactional
    @Override
    public void deletePicture(String pictureName, String pictureType, String email) {
        String filePath = email + "/" + pictureType + "/" + pictureName;
        BlobId blobId = BlobId.of(bucketName, filePath);
        try {
            storage.delete(blobId);
        } catch (Exception e) {
            log.error("Failed to delete receipt for user {}: {}", email, e.getMessage(), e);
            throw new BaseException(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void uploadToCloudStorage(MultipartFile file, String filePath, String contentType) {
        BlobId blobId = BlobId.of(bucketName, filePath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(contentType)
                .build();

        try (WriteChannel writer = storage.writer(blobInfo)) {
            writer.write(ByteBuffer.wrap(file.getBytes()));
        } catch (Exception e) {
            log.error("파일 업로드 실패 (파일 경로: {}): {}", filePath, e.getMessage(), e);
            throw new BaseException(BaseResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void replaceReceiptMetadata(String receiptPath, CreateReceiptRequest dto) {
        BlobId blobId = BlobId.of(bucketName, receiptPath);
        Blob blob = storage.get(blobId);

        if (blob == null) {
            throw new IllegalArgumentException("Storage에서 파일을 찾을 수 없음: " + receiptPath);
        }

        BlobInfo updatedBlobInfo = blob.toBuilder()
                .setMetadata(Map.of(
                        "category", dto.getCategory(),
                        "description", dto.getDescription()
                ))
                .build();

        storage.update(updatedBlobInfo);
    }

    private FileInfo createFileInfo(String filePath, String contentType) {
        return FileInfo.of(filePath, contentType);
    }

    private String generateFilePath(MultipartFile pictureFile, String pictureType,
            CreateReceiptRequest dto, CustomUserDetails customUserDetails) {
        String userEmail = customUserDetails.getUsername();
        String uniqueFileName = UUID.randomUUID() + "_" + pictureFile.getOriginalFilename();
        return userEmail + "/" + pictureType + "/" + dto.getDate() + "/" + uniqueFileName;
    }

    private void validateFile(MultipartFile pictureFile) {
        if (pictureFile.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }
    }

    @Override
    public FileInfo uploadProfilePicture(MultipartFile pictureFile, String pictureType, CustomUserDetails customUserDetails) {
        validateFile(pictureFile);

        String filePath = generateProfileFilePath(pictureFile);
        uploadToCloudStorage(pictureFile, filePath, pictureFile.getContentType());


        return FileInfo.of("https://storage.googleapis.com/" + bucketName + "/" + filePath, pictureFile.getContentType());
    }

    private String generateProfileFilePath(MultipartFile pictureFile) {
        return "profile-pictures/" + UUID.randomUUID() + "_" + pictureFile.getOriginalFilename();
    }

}

package com.walletguardians.walletguardiansapi.global.auth.cloudStorage.service;


import com.google.cloud.WriteChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.walletguardians.walletguardiansapi.domain.expenses.controller.dto.request.CreateReceiptRequest;
import com.walletguardians.walletguardiansapi.global.auth.CustomUserDetails;
import com.walletguardians.walletguardiansapi.global.exception.BaseException;
import com.walletguardians.walletguardiansapi.global.response.BaseResponseStatus;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudStorageService {

  private final Storage storage;

  @Value("${spring.cloud.gcp.storage.bucket}")
  private String bucketName;

  public void uploadPicture(MultipartFile pictureFile, String pictureType, CreateReceiptRequest dto,
      CustomUserDetails customUserDetails) {
    if (pictureFile.isEmpty()) {
      throw new IllegalArgumentException("Picture is empty");
    }
    String userEmail = customUserDetails.getUsername();
    String uniqueFileName = UUID.randomUUID() + "_" + pictureFile.getOriginalFilename();
    String filePath =
        userEmail + "/" + pictureType + "/" + dto.getDate() + "/"
            + uniqueFileName;
    String contentType = pictureFile.getContentType();

    BlobId blobId = BlobId.of(bucketName, filePath);
    BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
        .setContentType(contentType)
        .build();

    try (WriteChannel writer = storage.writer(blobInfo)) {
      byte[] fileData = pictureFile.getBytes();
      writer.write(ByteBuffer.wrap(fileData));
    } catch (Exception e) {
      log.error("Failed to upload picture for user {}: {}", userEmail, e.getMessage(), e);
      throw new BaseException(BaseResponseStatus.INTERNAL_SERVER_ERROR);
    }

    if (pictureType.equals("receipts")) {
      replaceReceiptMetadata(filePath, dto);
    }
  }

  public void replaceReceiptMetadata(String receiptPath, CreateReceiptRequest dto) {
    BlobId blobId = BlobId.of(bucketName, receiptPath);
    Blob blob = storage.get(blobId);

    if (blob == null) {
      throw new IllegalArgumentException("File not found in Storage: " + receiptPath);
    }

    BlobInfo updatedBlobInfo = blob.toBuilder()
        .setMetadata(Map.of(
            "category", dto.getCategory(),
            "description", dto.getDescription()
        ))
        .build();

    storage.update(updatedBlobInfo);
  }

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

}

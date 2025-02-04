package com.walletguardians.walletguardiansapi.global.auth.cloudStorage.service;


import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CloudStorageService {

  private final Storage storage;

  @Value("${spring.cloud.gcp.storage.bucket}")
  private String bucketName;

  public void createUserFolder(String username) throws
      StorageException {
    String folderPath = username + "/";
    BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, folderPath).build();
    storage.create(blobInfo);
  }

  public void createReceiptFolder(String username) throws
      StorageException {
    String folderPath = username + "/receipts/";
    BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, folderPath).build();
    storage.create(blobInfo);
  }

  public void createProfilePictureFolder(String username) throws
      StorageException {
    String folderPath = username + "/profilePicture/";
    BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, folderPath).build();
    storage.create(blobInfo);
  }

  public void createUserAndReceiptAndProfileFolder(String username) throws
      StorageException {
    createUserFolder(username);
    createReceiptFolder(username);
    createProfilePictureFolder(username);
  }



}

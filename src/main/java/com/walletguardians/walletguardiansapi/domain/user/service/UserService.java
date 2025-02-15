package com.walletguardians.walletguardiansapi.domain.user.service;

import com.walletguardians.walletguardiansapi.domain.budget.repository.BudgetRepository;
import com.walletguardians.walletguardiansapi.domain.expenses.repository.ExpenseRepository;
import com.walletguardians.walletguardiansapi.domain.expenses.service.CloudStorageService;
import com.walletguardians.walletguardiansapi.domain.expenses.service.dto.FileInfo;
import com.walletguardians.walletguardiansapi.domain.user.controller.dto.request.UpdateUserRequest;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import com.walletguardians.walletguardiansapi.domain.user.repository.UserRepository;
import com.walletguardians.walletguardiansapi.global.auth.CustomUserDetails;
import com.walletguardians.walletguardiansapi.global.auth.jwt.service.JwtService;
import com.walletguardians.walletguardiansapi.global.exception.BaseException;
import com.walletguardians.walletguardiansapi.global.response.BaseResponseStatus;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final ExpenseRepository expenseRepository;
  private final BudgetRepository budgetRepository;
  private final CloudStorageService cloudStorageService;


  @Transactional(readOnly = true)
  public User findUserByUserId(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MEMBER_ID));
  }

  @Transactional(readOnly = true)
  public User findUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MEMBER_ID));
  }

  @Transactional
  public void logout(String accessToken, String email) {
    boolean expiration = jwtService.validateToken(accessToken);
    if (expiration) {
      jwtService.deleteRefreshTokenByEmail(email);
    } else {
      throw new BaseException(BaseResponseStatus.NOT_FOUND_MEMBER_ID);
    }
  }

  @Transactional
  public void deleteById(Long userId) {
    userRepository.deleteById(userId);
  }

  @Transactional
  public User updatePassword(Long userId, UpdateUserRequest updateUserRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MEMBER_ID));
    validatePassword(user, updateUserRequest);
    user.updatePassword(passwordEncoder, updateUserRequest.getNewPassword());
    return user;
  }

  private void validatePassword(User user, UpdateUserRequest updateUserRequest) {
    if (!user.isPasswordValid(passwordEncoder, updateUserRequest.getPassword())) {
      throw new BaseException(BaseResponseStatus.UNAUTHORIZED);
    }
    if (user.isPasswordValid(passwordEncoder, updateUserRequest.getNewPassword())) {
      throw new BaseException(BaseResponseStatus.SAME_PASSWORD);
    }
  }

  @Scheduled(cron = "0 0 0 1 * ?") // 매월 1일 자정에 실행
  @Transactional
  public void updateUserDefense() {
    LocalDate lastMonth = LocalDate.now().minusMonths(1);
    List<User> users = userRepository.findAll();
    users.forEach(user -> updateDefenseForUser(user, lastMonth));
  }

  private void updateDefenseForUser(User user, LocalDate lastMonth) {
    int totalSpent = expenseRepository.getTotalSpentByUserIdAndMonth(user.getId(), lastMonth);
    Integer budget = budgetRepository.getBudgetByUserIdAndMonth(user.getId(), lastMonth);
    int budgetAmount = (budget != null) ? budget : 0;

    if (totalSpent > budgetAmount) {
      user.decreaseDefense(6);
    } else if (totalSpent < budgetAmount) {
      user.increaseDefense(10);
    }
  }

  @Transactional
  public String uploadProfilePicture(Long userId, MultipartFile file, CustomUserDetails customUserDetails) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MEMBER_ID));

    FileInfo fileInfo = cloudStorageService.uploadProfilePicture(file, "profile-pictures", customUserDetails);

    user.updateProfileImage(fileInfo.getFilePath());
    userRepository.save(user);

    return fileInfo.getFilePath();
  }
  
  @Transactional
  public String updateProfilePicture(Long userId, MultipartFile file, CustomUserDetails customUserDetails) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MEMBER_ID));

    if (user.getProfileImagePath() != null) {
      String bucketName = cloudStorageService.getBucketName();
      String oldImagePath = user.getProfileImagePath().replace("https://storage.googleapis.com/" + bucketName + "/", "");
      cloudStorageService.deletePicture(oldImagePath, "profile-pictures", user.getEmail());
    }

    FileInfo fileInfo = cloudStorageService.uploadProfilePicture(file, "profile-pictures", customUserDetails);
    user.updateProfileImage(fileInfo.getFilePath());
    userRepository.save(user);

    return fileInfo.getFilePath();
  }

  @Transactional
  public void deleteProfilePicture(Long userId, CustomUserDetails customUserDetails) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FOUND_MEMBER_ID));

    if (user.getProfileImagePath() != null) {
      String oldImagePath = user.getProfileImagePath().replace("https://storage.googleapis.com/" + cloudStorageService.getBucketName() + "/", "");
      cloudStorageService.deletePicture(oldImagePath, "profile-pictures", customUserDetails.getUsername());
    }

    user.updateProfileImage(null);
    userRepository.save(user);
  }
}

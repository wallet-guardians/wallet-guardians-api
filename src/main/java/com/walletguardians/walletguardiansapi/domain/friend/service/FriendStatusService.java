package com.walletguardians.walletguardiansapi.domain.friend.service;

import com.walletguardians.walletguardiansapi.domain.friend.controller.dto.request.SenderRequest;
import com.walletguardians.walletguardiansapi.domain.friend.controller.dto.response.ReceiverResponse;
import com.walletguardians.walletguardiansapi.domain.friend.controller.dto.response.SenderResponse;
import com.walletguardians.walletguardiansapi.domain.friend.entity.Friend;
import com.walletguardians.walletguardiansapi.domain.friend.entity.FriendStatus;
import com.walletguardians.walletguardiansapi.domain.friend.entity.status.FriendStatusEnum;
import com.walletguardians.walletguardiansapi.domain.friend.repository.FriendRepository;
import com.walletguardians.walletguardiansapi.domain.friend.repository.FriendStatusRepository;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import com.walletguardians.walletguardiansapi.domain.user.service.UserService;
import com.walletguardians.walletguardiansapi.global.auth.CustomUserDetails;
import com.walletguardians.walletguardiansapi.global.exception.BaseException;
import com.walletguardians.walletguardiansapi.global.response.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendStatusService {

  private final FriendStatusRepository friendStatusRepository;
  private final UserService userService;
  private final FriendRepository friendRepository;
  private final FriendService friendService;

  @Transactional
  public SenderResponse sendFriendRequest(CustomUserDetails customUserDetails,
      SenderRequest senderRequest) {
    User sender = userService.findUserByEmail(customUserDetails.getUsername());
    User receiver = userService.findUserByEmail(senderRequest.getReceiverEmail());

    if (sender.equals(receiver)) {
      throw new BaseException(BaseResponseStatus.CAN_NOT_SEND_YOURSELF);
    }

    this.checkFriendStatusAndFriendExists(sender, receiver);
    FriendStatus pendingFriendStatus = this.createFriendStatusEntity(sender, receiver);

    friendStatusRepository.save(pendingFriendStatus);
    return SenderResponse.fromEntity(pendingFriendStatus);
  }

  @Transactional
  public void acceptFriendRequest(CustomUserDetails customUserDetails,
      Long friendStatusId) {
    User user = customUserDetails.getUser();
    FriendStatus foundFriendStatus = friendStatusRepository.findByIdAndReceiver(friendStatusId, user)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_FRIEND_REQUEST));

    User friend = foundFriendStatus.getSender();

    if (foundFriendStatus.getFriendStatus().equals(FriendStatusEnum.REJECTED)) {
      throw new BaseException(BaseResponseStatus.ALREADY_REJECTED);
    }

    this.saveFriend(user, friend);
    this.saveFriend(friend, user);
    friendStatusRepository.delete(foundFriendStatus);
  }

  @Transactional
  public void rejectFriendRequest(CustomUserDetails customUserDetails,
      Long friendStatusId) {
    FriendStatus foundFriendStatus = friendStatusRepository.findByIdAndReceiver(friendStatusId,
            customUserDetails.getUser())
        .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_FRIEND_REQUEST));

    if (foundFriendStatus.getFriendStatus().equals(FriendStatusEnum.REJECTED)) {
      throw new BaseException(BaseResponseStatus.ALREADY_REJECTED);
    }

    foundFriendStatus.updateFriendStatus(FriendStatusEnum.REJECTED);
  }

  @Transactional
  public void cancelFriendRequest(CustomUserDetails customUserDetails,
      Long friendStatusId) {
    FriendStatus foundFriendStatus = friendStatusRepository.findByIdAndSender(friendStatusId,
            customUserDetails.getUser())
        .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_FRIEND_REQUEST));

    friendStatusRepository.delete(foundFriendStatus);
  }

  @Transactional
  public List<ReceiverResponse> getReceivedRequests(CustomUserDetails customUserDetails) {
    return friendStatusRepository.findByReceiverAndFriendStatus(customUserDetails.getUser(), FriendStatusEnum.PENDING)
        .stream()
        .map(ReceiverResponse::fromEntity)
        .toList();
  }

  @Transactional
  public List<SenderResponse> getSentRequests(CustomUserDetails customUserDetails) {
    return friendStatusRepository.findBySender(customUserDetails.getUser())
        .stream()
        .map(SenderResponse::fromEntity)
        .toList();
  }

  private void checkFriendStatusAndFriendExists(User user, User friend) {
    checkFriendStatusExists(user, friend);
    checkFriendExists(user, friend);
  }

  private void checkFriendStatusExists(User sender, User receiver) {
    boolean exists = friendStatusRepository.existsBySenderAndReceiver(sender, receiver) ||
        friendStatusRepository.existsBySenderAndReceiver(receiver, sender);
    if (exists) {
      throw new BaseException(BaseResponseStatus.ALREADY_FRIEND_REQUEST_SENT_OR_FRIEND);
    }
  }

  private void checkFriendExists(User user, User friend) {
    boolean exists = friendRepository.existsByUserEntityAndFriendEntity(user, friend);
    if (exists) {
      throw new BaseException(BaseResponseStatus.ALREADY_FRIEND_REQUEST_SENT_OR_FRIEND);
    }
  }

  private FriendStatus createFriendStatusEntity(User sender, User receiver) {
    return FriendStatus.builder()
        .sender(sender)
        .receiver(receiver)
        .friendStatus(FriendStatusEnum.PENDING)
        .build();
  }

  private void saveFriend(User user, User friend){
    Friend friendEntity = friendService.createFriendEntity(user, friend);
    friendRepository.save(friendEntity);
  }

}


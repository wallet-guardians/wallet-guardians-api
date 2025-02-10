package com.walletguardians.walletguardiansapi.domain.friend.service;

import com.walletguardians.walletguardiansapi.domain.friend.controller.dto.request.FriendSenderRequest;
import com.walletguardians.walletguardiansapi.domain.friend.controller.dto.request.FriendReceiverRequest;
import com.walletguardians.walletguardiansapi.domain.friend.controller.dto.response.FriendResponse;
import com.walletguardians.walletguardiansapi.domain.friend.entity.Friend;
import com.walletguardians.walletguardiansapi.domain.friend.entity.FriendStatusEnum;
import com.walletguardians.walletguardiansapi.domain.friend.repository.FriendRepository;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import com.walletguardians.walletguardiansapi.domain.user.service.UserService;
import com.walletguardians.walletguardiansapi.global.auth.CustomUserDetails;
import com.walletguardians.walletguardiansapi.global.exception.BaseException;
import com.walletguardians.walletguardiansapi.global.response.BaseResponseStatus;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

  private final FriendRepository friendRepository;
  private final UserService userService;

  @Transactional
  public FriendResponse sendFriendRequest(CustomUserDetails customUserDetails,
      FriendSenderRequest friendSenderRequest) {
    User sender = userService.findUserByEmail(customUserDetails.getUsername());
    User receiver = userService.findUserByEmail(friendSenderRequest.getReceiverEmail());

    if (sender.equals(receiver)){
      throw new BaseException(BaseResponseStatus.CAN_NOT_SEND_YOURSELF);
    }

    this.checkFriendStatusExists(sender, receiver);
    Friend pendingFriend = this.createFriendEntity(sender, receiver);

    friendRepository.save(pendingFriend);
    return FriendResponse.fromEntity(pendingFriend);
  }

  @Transactional
  public void acceptFriendRequest(CustomUserDetails customUserDetails,
      FriendReceiverRequest friendReceiverRequest) {
    User sender = userService.findUserByEmail(friendReceiverRequest.getSenderEmail());
    User receiver = userService.findUserByEmail(customUserDetails.getUsername());

    Friend foundFriend = friendRepository.findBySenderAndReceiver(sender, receiver)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_FRIEND_REQUEST));

    if (foundFriend.getFriendStatus().equals(FriendStatusEnum.ACCEPTED)) {
      throw new BaseException(BaseResponseStatus.ALREADY_ACCEPTED);
    }
    foundFriend.updateFriendStatus(FriendStatusEnum.ACCEPTED);
  }

  @Transactional
  public void rejectFriendRequest(CustomUserDetails customUserDetails,
      FriendReceiverRequest friendReceiverRequest) {
    User receiver = userService.findUserByEmail(customUserDetails.getUsername());
    User sender = userService.findUserByEmail(friendReceiverRequest.getSenderEmail());

    Friend foundFriend = friendRepository.findBySenderAndReceiver(sender, receiver)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_FRIEND_REQUEST));

    if (foundFriend.getFriendStatus().equals(FriendStatusEnum.ACCEPTED)) {
      throw new BaseException(BaseResponseStatus.ALREADY_ACCEPTED);
    }
    friendRepository.delete(foundFriend);
  }

  @Transactional
  public void deleteFriend(CustomUserDetails customUserDetails,
      FriendReceiverRequest friendReceiverRequest) {
    User sender = userService.findUserByEmail(friendReceiverRequest.getSenderEmail());
    User receiver = userService.findUserByEmail(customUserDetails.getUsername());

    Friend foundFriend = friendRepository.findBySenderAndReceiver(sender, receiver)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_FRIEND));

    if (foundFriend.getFriendStatus().equals(FriendStatusEnum.PENDING)) {
      throw new BaseException(BaseResponseStatus.NO_FRIEND);
    }

    friendRepository.delete(foundFriend);
  }

  @Transactional
  public void cancelFriendRequest(CustomUserDetails customUserDetails,
      FriendSenderRequest friendSenderRequest) {
    User sender = userService.findUserByEmail(customUserDetails.getUsername());
    User receiver = userService.findUserByEmail(friendSenderRequest.getReceiverEmail());

    Friend foundFriend = friendRepository.findBySenderAndReceiver(sender, receiver)
        .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_FRIEND_REQUEST));

    if (foundFriend.getFriendStatus().equals(FriendStatusEnum.ACCEPTED)) {
      throw new BaseException(BaseResponseStatus.NO_FRIEND_REQUEST);
    }

    friendRepository.delete(foundFriend);

  }

  @Transactional(readOnly = true)
  public List<FriendResponse> getAcceptedOrPendingOrReceivedRequest(
      CustomUserDetails customUserDetails, String status) {
    Map<String, Function<String, List<FriendResponse>>> statusHandler = Map.of("accepted",
        this::getAcceptedFriends, "pending", this::getSendingRequests, "requested",
        this::getReceivedRequests);

    Function<String, List<FriendResponse>> handler = statusHandler.get(
        status.toLowerCase());

    if (handler == null) {
      throw new BaseException(BaseResponseStatus.INVALID_FRIEND_STATUS);
    }

    return handler.apply(customUserDetails.getUsername());
  }

  public List<FriendResponse> getAcceptedFriends(String userEmail) {
    return friendRepository.findAcceptedFriends(userEmail, FriendStatusEnum.ACCEPTED)
        .stream()
        .map(FriendResponse::fromEntity)
        .collect(Collectors.toList());
  }

  public List<FriendResponse> getSendingRequests(String userEmail) {
    return friendRepository.findPendingRequestsBySender(userEmail,
            FriendStatusEnum.PENDING)
        .stream()
        .map(FriendResponse::fromEntity)
        .collect(Collectors.toList());
  }

  public List<FriendResponse> getReceivedRequests(String userEmail) {
    return friendRepository.findPendingRequestsByReceiver(userEmail,
            FriendStatusEnum.PENDING)
        .stream()
        .map(FriendResponse::fromEntity)
        .collect(Collectors.toList());
  }

  private void checkFriendStatusExists(User sender, User receiver) {
    boolean exists = friendRepository.existsBySenderAndReceiver(sender, receiver) ||
        friendRepository.existsBySenderAndReceiver(receiver, sender);
    if (exists) {
      throw new BaseException(BaseResponseStatus.ALREADY_FRIEND_REQUEST_SENT_OR_FRIEND);
    }
  }

  private Friend createFriendEntity(User sender, User receiver) {
    return Friend.builder()
        .sender(sender)
        .receiver(receiver)
        .friendStatus(FriendStatusEnum.PENDING)
        .build();
  }

}


package com.walletguardians.walletguardiansapi.domain.friend.service;

import com.walletguardians.walletguardiansapi.domain.friend.controller.dto.response.FriendResponse;
import com.walletguardians.walletguardiansapi.domain.friend.entity.Friend;
import com.walletguardians.walletguardiansapi.domain.friend.repository.FriendRepository;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import com.walletguardians.walletguardiansapi.domain.user.service.UserService;
import com.walletguardians.walletguardiansapi.global.security.CustomUserDetails;
import com.walletguardians.walletguardiansapi.global.exception.BaseException;
import com.walletguardians.walletguardiansapi.global.response.BaseResponseStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendService {

  private final FriendRepository friendRepository;
  private final UserService userService;

  @Transactional
  public List<FriendResponse> getAllFriends(CustomUserDetails customUserDetails) {
    User user = userService.findUserByUserId(customUserDetails.getUserId());
    List<Friend> foundFriends = friendRepository.findByUserEntity(user);

    return foundFriends
        .stream()
        .map(FriendResponse::fromEntity)
        .toList();
  }

  @Transactional
  public void deleteFriend(CustomUserDetails customUserDetails,
      Long friendListId) {
    User user = customUserDetails.getUser();
    Friend foundFriend = friendRepository.findByIdAndUserEntity(friendListId, customUserDetails.getUser())
        .orElseThrow(() -> new BaseException(BaseResponseStatus.NO_FRIEND));
    User friend = foundFriend.getFriendEntity();

    friendRepository.deleteByUserEntityAndFriendEntity(user, friend);
    friendRepository.deleteByUserEntityAndFriendEntity(friend, user);
  }

  @Transactional
  public Friend createFriendEntity(User user, User friend) {
    return Friend.builder()
        .userEntity(user)
        .friendEntity(friend)
        .build();
  }

}

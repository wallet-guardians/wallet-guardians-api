package com.walletguardians.walletguardiansapi.domain.friend.controller;

import com.walletguardians.walletguardiansapi.domain.friend.entity.FriendshipStatus;
import com.walletguardians.walletguardiansapi.domain.friend.service.FriendshipService;
import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import com.walletguardians.walletguardiansapi.domain.user.service.UserService;
import com.walletguardians.walletguardiansapi.global.auth.jwt.service.JwtService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipService friendshipService;
    private final UserService userService;
    private final JwtService jwtService; // JwtService 주입

    @PostMapping("/request")
    public FriendshipStatus sendFriendRequest(@RequestHeader("ACCESS-AUTH-KEY") String accessToken,
        @RequestBody Map<String, String> requestBody) {
        String receiverEmail = requestBody.get("receiverEmail");
        String senderEmail = jwtService.getUserPk(accessToken.replace("Bearer ", ""));
        User sender = userService.findUserByEmail(senderEmail);
        User receiver = userService.findUserByEmail(receiverEmail);
        return friendshipService.sendFriendRequest(sender, receiver);
    }


    // 친구 요청 수락
    @PutMapping("/accept/{requestId}")
    public FriendshipStatus acceptFriendRequest(@PathVariable Long requestId) {
        FriendshipStatus friendshipStatus = friendshipService.findById(requestId);
        return friendshipService.acceptFriendRequest(friendshipStatus);
    }

    // 팔로잉 목록 조회
    @GetMapping("/following")
    public List<FriendshipStatus> getFollowingList(@RequestHeader("Authorization") String token) {
        String email = jwtService.getUserPk(token.replace("Bearer ", ""));
        User user = userService.findUserByEmail(email);
        return friendshipService.getFollowingList(user);
    }

    // 팔로워 목록 조회
    @GetMapping("/followers")
    public List<FriendshipStatus> getFollowerList(@RequestHeader("Authorization") String token) {
        String email = jwtService.getUserPk(token.replace("Bearer ", ""));
        User user = userService.findUserByEmail(email);
        return friendshipService.getFollowerList(user);
    }
}

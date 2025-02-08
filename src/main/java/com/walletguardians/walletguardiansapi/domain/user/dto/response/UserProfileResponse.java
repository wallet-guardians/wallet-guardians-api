package com.walletguardians.walletguardiansapi.domain.user.dto.response;

import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserProfileResponse {
    private final Long id;
    private final String username;
    private final String email;
    private final String title;
    private final double defenseRate;
    private final boolean userDeleted;

    public UserProfileResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.title = user.getTitle();
        this.defenseRate = user.getDefenseRate();
        this.userDeleted = user.isUserDeleted();
    }
}

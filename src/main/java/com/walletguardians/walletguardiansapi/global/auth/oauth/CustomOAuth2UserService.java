package com.walletguardians.walletguardiansapi.global.auth.oauth;

import com.walletguardians.walletguardiansapi.domain.user.entity.User;
import com.walletguardians.walletguardiansapi.domain.user.entity.auth.Role;
import com.walletguardians.walletguardiansapi.domain.user.repository.UserRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

    Map<String, Object> attributes = oAuth2User.getAttributes();
    String userEmail = (String) attributes.get("email");
    String username = (String) attributes.get("name");

    User user = userRepository.findByEmail(userEmail)
        .orElseGet(() -> createUser(userEmail, username));

    return new CustomOAuth2User(user, attributes);
  }

  private User createUser(String userEmail, String username) {
    User user = User.builder()
        .email(userEmail)
        .username(username)
        .role(Role.USER)
        .build();

    return userRepository.save(user);

  }

}

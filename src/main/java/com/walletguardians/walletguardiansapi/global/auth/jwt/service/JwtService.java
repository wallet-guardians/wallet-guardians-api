package com.walletguardians.walletguardiansapi.global.auth.jwt.service;

import com.walletguardians.walletguardiansapi.global.auth.jwt.dto.TokenDto;
import com.walletguardians.walletguardiansapi.global.auth.jwt.entity.RefreshToken;
import com.walletguardians.walletguardiansapi.global.auth.jwt.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

  private static final String ACCESS_TOKEN_HEADER = "ACCESS-AUTH-KEY";
  private static final String REFRESH_TOKEN_HEADER = "REFRESH-AUTH-KEY";
  private static final String BEARER = "BEARER";
  private static final String ACCESS_TOKEN_TYPE = "Access";
  private static final String REFRESH_TOKEN_TYPE = "Refresh";

  @Value("${jwt.secret-Key}")
  private String secretKey;
  private Key key;

  private final UserDetailsService userDetailsService;
  private final RefreshTokenRepository refreshTokenRepository;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;

  private static final long ACCESS_TIME = 30 * 60 * 1000L; //30분
  private static final long REFRESH_TIME = 40 * 60 * 1000L;

  @PostConstruct
  protected void init() {
    key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  public TokenDto signIn(String email, String password) {
    //principal (사용자명), credentials (비밀번호)
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
        email, password);

    // 실제 검증 (사용자 비밀번호 체크)
    // authenticate 메서드가 실행될 때 Custo,UserDetailsService에서 만든 loadUserByUsername 메서드가 실행
    Authentication authentication = authenticationManagerBuilder.getObject()
        .authenticate(authenticationToken);
    TokenDto tokenDto = createAllToken(authentication);
    saveRefreshTokenToRepo(authentication.getName(), tokenDto.getRefreshToken());

    return tokenDto;
  }

  public TokenDto oauthSignIn(OAuth2AuthenticationToken authenticationToken) {
    Authentication authentication = new UsernamePasswordAuthenticationToken(
        authenticationToken.getPrincipal().getAttribute("email"), null);

    TokenDto tokenDto = createAllToken(authentication);
    saveRefreshTokenToRepo(authentication.getName(), tokenDto.getRefreshToken());

    return tokenDto;
  }

  // 토큰 생성 order -> dto에 담음
  public TokenDto createAllToken(Authentication authentication) {
    String authorities = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority).collect(
            Collectors.joining(","));

    return new TokenDto(createToken(authentication.getName(), authorities, ACCESS_TOKEN_TYPE),
        createToken(authentication.getName(), authorities, REFRESH_TOKEN_TYPE));
  }

  // 헤더에 있는 access 토큰 추출
  public Optional<String> extractAccessToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(ACCESS_TOKEN_HEADER))
        .filter(accessToken -> accessToken.startsWith(BEARER))
        .map(accessToken -> accessToken.replace(BEARER, ""));
  }

  // 헤더에 있는 refresh 토큰 추출
  public Optional<String> extractRefreshToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(REFRESH_TOKEN_HEADER))
        .filter(refreshToken -> refreshToken.startsWith(BEARER))
        .map(refreshToken -> refreshToken.replace(BEARER, ""));
  }

  public Authentication getAuthentication(String token) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getUserPk(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  public boolean validateToken(String jwtToken) {
    try {
      // JWT 토큰을 파싱하고, 서명 키를 사용해 서명을 검증
      Jws<Claims> claims = Jwts.parserBuilder()
          .setSigningKey(key) // 비밀 키 설정
          .build()
          .parseClaimsJws(jwtToken);

      // 토큰 만료 시간 검증
      return !claims.getBody().getExpiration().before(new Date());
    } catch (ExpiredJwtException e) {
      log.error("토큰이 만료되었습니다: {}", jwtToken, e);
    } catch (MalformedJwtException e) {
      log.error("잘못된 JWT 형식입니다: {}", jwtToken, e);
    } catch (SignatureException e) {
      log.error("서명이 올바르지 않습니다: {}", jwtToken, e);
    } catch (Exception e) {
      log.error("JWT 토큰 검증 중 오류 발생: {}", jwtToken, e);
    }

    // 예외 발생 시 false 반환
    return false;
  }

  // 토큰 생성 (access, refresh)
  private String createToken(String email, String authorities, String type) {
    long expiration = type.equals(ACCESS_TOKEN_TYPE) ? ACCESS_TIME : REFRESH_TIME;

    Date now = new Date();
    long nowTime = now.getTime();
    Date validity = new Date(nowTime + expiration);

    return Jwts.builder()
        .setSubject(email)
        .claim("role", authorities)
        .setIssuedAt(now)
        .setExpiration(validity)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  private void saveRefreshTokenToRepo(String email, String refreshToken) {
    RefreshToken refreshTokenEntity = new RefreshToken(email, refreshToken);
    refreshTokenRepository.save(refreshTokenEntity);
  }

  @Transactional
  public void deleteRefreshTokenByEmail(String email) {
    RefreshToken refreshToken = refreshTokenRepository.findByUserEmail(email)
        .orElseThrow(() -> new IllegalArgumentException(
            "No refresh token found for user with email: " + email));
    refreshTokenRepository.delete(refreshToken);
  }

}

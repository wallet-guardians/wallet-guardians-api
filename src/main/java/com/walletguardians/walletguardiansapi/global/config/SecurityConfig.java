package com.walletguardians.walletguardiansapi.global.config;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.walletguardians.walletguardiansapi.global.auth.jwt.filter.JwtAuthenticationFilter;
import com.walletguardians.walletguardiansapi.global.auth.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtService jwtService;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // 특정 HTTP 요청에 대한 웹 기반 보안 구성
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector)
      throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
            SessionCreationPolicy.STATELESS))
        // 인증되지 않은 요청에 대한 에러 (401 에러, exception 커스텀 가능)
        .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(
            HttpStatus.UNAUTHORIZED)))

        /**
         * 아래에 적혀있는 url은 인증 없이 접근 가능
         * 이외의 url은 security에 의해 제한됨
         */
        .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
            .requestMatchers(new MvcRequestMatcher(introspector, "/api/auth")).permitAll()
            .requestMatchers(new MvcRequestMatcher(introspector, "/api/auth/login")).permitAll()
            .requestMatchers(new MvcRequestMatcher(introspector, "/api/auth/sign-up")).permitAll()
            .anyRequest().authenticated())
        // jwtFilter 후 UsernamePasswordAuthenticationFilter 인증 처리
        .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  // 인증 관리자 관련 설정
  @Bean
  public JwtAuthenticationFilter jwtFilter() {
    return new JwtAuthenticationFilter(jwtService);
  }

  // GCS 관련 의존성 주입을 위한 설정
  @Bean
  public Storage storage() {
    return StorageOptions.getDefaultInstance().getService();
  }
}

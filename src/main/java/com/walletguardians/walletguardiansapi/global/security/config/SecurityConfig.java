package com.walletguardians.walletguardiansapi.global.security.config;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.walletguardians.walletguardiansapi.global.auth.jwt.filter.JwtAuthenticationFilter;
import com.walletguardians.walletguardiansapi.global.auth.jwt.service.JwtService;
import com.walletguardians.walletguardiansapi.global.auth.oauth.CustomOAuth2UserService;
import java.util.Arrays;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtService jwtService;
  private final CustomOAuth2UserService customOAuth2UserService;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector)
      throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
            SessionCreationPolicy.STATELESS))
        .exceptionHandling(e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(
            HttpStatus.UNAUTHORIZED)))
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
            .requestMatchers(new MvcRequestMatcher(introspector, "/api/auth")).permitAll()
            .requestMatchers(new MvcRequestMatcher(introspector, "/api/auth/login")).permitAll()
            .requestMatchers(new MvcRequestMatcher(introspector, "/api/auth/signup")).permitAll()
            .requestMatchers(new MvcRequestMatcher(introspector, "/api/auth/google/login"))
            .permitAll()
            .anyRequest().authenticated())
        .oauth2Login(oauth -> oauth.userInfoEndpoint(
                userInfo -> userInfo.userService(customOAuth2UserService))
            .successHandler((request, response, authentication) ->
                response.sendRedirect("/api/auth/google/login")
            ))
        .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  // 인증 관리자 관련 설정
  @Bean
  public JwtAuthenticationFilter jwtFilter() {
    return new JwtAuthenticationFilter(jwtService);
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
//    configuration.setAllowedOrigins(
//        Arrays.asList("http://localhost:5173", "https://wallet-guardians.vercel.app",
//            "https://wallet-guardians.vercel.app/login"));
    configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
    configuration.setAllowCredentials(true);
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  // GCS 관련 의존성 주입을 위한 설정
  @Bean
  public Storage storage() {
    return StorageOptions.getDefaultInstance().getService();
  }
}

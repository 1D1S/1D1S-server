package com.odos.odos_server.security.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odos.odos_server.member.entity.Member;
import com.odos.odos_server.member.enums.Provider;
import com.odos.odos_server.member.repository.MemberRepository;
import com.odos.odos_server.security.jwt.JwtTokenProvider;
import com.odos.odos_server.security.oauth2.CustomOAuth2User;
import com.odos.odos_server.security.oauth2.OAuth2LoginResponse;
import com.odos.odos_server.security.oauth2.service.CustomOAuth2UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final JwtTokenProvider jwtTokenProvider;
  private final MemberRepository memberRepository;
  private final ObjectMapper objectMapper;
  private final CustomOAuth2UserService customOAuth2UserService;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

    String registrationId = oauthToken.getAuthorizedClientRegistrationId();
    Provider provider = Provider.valueOf(registrationId.toUpperCase());

    CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
    String email = oAuth2User.getEmail();
    String socialId = oAuth2User.getName();

    Member member =
        memberRepository
            .findByEmail(email)
            .orElseGet(() -> customOAuth2UserService.createMember(email, provider, socialId));

    issueTokensForExistingUser(response, member.getEmail());
  }

  private void issueTokensForExistingUser(HttpServletResponse response, String email)
      throws IOException {
    Member member =
        memberRepository
            .findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));

    // 새 AccessToken 항상 발급
    String accessToken = jwtTokenProvider.createAccessToken(member);

    // 기존 RefreshToken 검사
    String existingRefresh = member.getRefreshToken();
    boolean needNew =
        existingRefresh == null
            || !jwtTokenProvider.isValidToken(existingRefresh)
            || jwtTokenProvider.isExpired(existingRefresh);

    String refreshToken;
    if (needNew) {
      refreshToken = jwtTokenProvider.createRefreshToken();
      jwtTokenProvider.updateRefreshToken(email, refreshToken);
    } else {
      refreshToken = existingRefresh;
    }

    log.debug("▶︎ accessToken  = {}", accessToken);
    log.debug("▶︎ refreshToken = {}", refreshToken);

    response.setHeader("Authorization", "Bearer " + accessToken);
    response.setHeader("Authorization-Refresh", "Bearer " + refreshToken);

    OAuth2LoginResponse dto =
        OAuth2LoginResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().write(objectMapper.writeValueAsString(dto));
  }
}

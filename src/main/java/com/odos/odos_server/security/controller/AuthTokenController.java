package com.odos.odos_server.security.controller;

import com.odos.odos_server.member.repository.MemberRepository;
import com.odos.odos_server.security.jwt.JwtTokenProvider;
import com.odos.odos_server.security.oauth2.OAuth2LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthTokenController {

  private final JwtTokenProvider jwtTokenProvider;
  private final MemberRepository memberRepository;

  @PostMapping("/token")
  public ResponseEntity<OAuth2LoginResponse> reissueAccessToken(
      @RequestHeader("Authorization-Refresh") String refreshHeader) {

    String refreshToken =
        jwtTokenProvider
            .extractTokenFromHeader(refreshHeader)
            .orElseThrow(() -> new JwtException("RefreshToken is missing or invalid format"));

    if (!jwtTokenProvider.isValidToken(refreshToken) || jwtTokenProvider.isExpired(refreshToken)) {
      throw new JwtException("RefreshToken is invalid or expired");
    }

    return memberRepository
        .findByRefreshToken(refreshToken)
        .map(
            member -> {
              String newAccessToken = jwtTokenProvider.createAccessToken(member);
              String newRefreshToken = jwtTokenProvider.createRefreshToken();
              jwtTokenProvider.updateRefreshToken(member.getEmail(), newRefreshToken);

              OAuth2LoginResponse response =
                  OAuth2LoginResponse.builder()
                      .accessToken(newAccessToken)
                      .refreshToken(newRefreshToken)
                      .build();

              return ResponseEntity.ok(response);
            })
        .orElseThrow(() -> new JwtException("RefreshToken does not match any user"));
  }
}

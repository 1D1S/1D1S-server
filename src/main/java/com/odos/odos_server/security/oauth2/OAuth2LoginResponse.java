package com.odos.odos_server.security.oauth2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2LoginResponse {
  private String accessToken;
  private String refreshToken;
  private boolean isProfileComplete;
}

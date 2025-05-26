package com.odos.odos_server.security.oauth2.info;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {
  public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
    super(attributes);
  }

  @Override
  public String getId() {
    return String.valueOf(attributes.get("id"));
  }

  @Override
  @SuppressWarnings("unchecked")
  public String getEmail() {
    Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
    return account != null ? (String) account.get("email") : null;
  }
}

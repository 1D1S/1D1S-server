package com.odos.odos_server.security.oauth2.info;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {
  public NaverOAuth2UserInfo(Map<String, Object> attributes) {
    super((Map<String, Object>) attributes.get("response"));
  }

  @Override
  public String getId() {
    return attributes.get("id").toString();
  }

  @Override
  @SuppressWarnings("unchecked")
  public String getEmail() {
    return attributes.get("email").toString();
  }
}

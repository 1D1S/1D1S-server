package com.odos.odos_server.security.oauth2.info;

import com.odos.odos_server.member.enums.Provider;
import java.util.Map;

public class OAuth2UserInfoFactory {

  public static OAuth2UserInfo get(Provider provider, Map<String, Object> attributes) {
    return switch (provider) {
      case GOOGLE -> new GoogleOAuth2UserInfo(attributes);
      case KAKAO -> new KakaoOAuth2UserInfo(attributes);
      case NAVER -> new NaverOAuth2UserInfo(attributes);
      default -> throw new IllegalArgumentException("unexpected provider " + provider);
    };
  }
}

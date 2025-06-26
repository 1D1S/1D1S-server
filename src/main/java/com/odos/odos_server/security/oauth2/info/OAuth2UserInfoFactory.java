package com.odos.odos_server.security.oauth2.info;

import com.odos.odos_server.member.enums.SignupRoute;
import java.util.Map;

public class OAuth2UserInfoFactory {

  public static OAuth2UserInfo get(SignupRoute signupRoute, Map<String, Object> attributes) {
    return switch (signupRoute) {
      case GOOGLE -> new GoogleOAuth2UserInfo(attributes);
      case KAKAO -> new KakaoOAuth2UserInfo(attributes);
      case NAVER -> new NaverOAuth2UserInfo(attributes);
      default -> throw new IllegalArgumentException("unexpected provider " + signupRoute);
    };
  }
}

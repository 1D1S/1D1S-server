package com.odos.odos_server.security.oauth2.info;

import com.odos.odos_server.domain.common.Enum.SignupRoute;
import com.odos.odos_server.error.code.ErrorCode;
import com.odos.odos_server.error.exception.CustomException;
import java.util.Map;

public class OAuth2UserInfoFactory {

  public static OAuth2UserInfo get(SignupRoute signupRoute, Map<String, Object> attributes) {
    return switch (signupRoute) {
      case GOOGLE -> new GoogleOAuth2UserInfo(attributes);
      case KAKAO -> new KakaoOAuth2UserInfo(attributes);
      case NAVER -> new NaverOAuth2UserInfo(attributes);
      default -> throw new CustomException(ErrorCode.INVALID_SIGNUP_PROVIDER);
    };
  }
}

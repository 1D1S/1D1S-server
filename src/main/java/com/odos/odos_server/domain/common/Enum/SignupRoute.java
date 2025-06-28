package com.odos.odos_server.domain.common.Enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SignupRoute {
  KAKAO,
  GOOGLE,
  NAVER,
  APPLE
}

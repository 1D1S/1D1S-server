package com.odos.odos_server.member.enums;

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
